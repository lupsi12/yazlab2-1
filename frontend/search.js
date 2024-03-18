const MAX_ARTICLE = 10;
const enableSerbia = true;

let foundArticles = 0;
let checkIfReadyInterval;
let searchID;
let autoPDF;

const waitingSymbols = ["-", "\\", "|", "/"];
let waitingState = 0;

async function onLoad()
{
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	searchID = linkParameters.get("searchID");

    document.getElementById("typoText").classList.add("text-center");
	checkIfReadyInterval = setInterval(CheckIfReady, 250);
}

async function HandleSearchResults()
{
    let articleListRes = await fetch("http://localhost:8080/veri");
    let articleList = await articleListRes.json();

    articleList = articleList.slice(Math.max(articleList.length - foundArticles, 0));

    console.log(articleList);
    articleList.forEach(AddResultToList);
}

function AddResultToList(value, index, array)
{
	let searchResults = document.getElementById("searchResults");

    // Arama Sonucu Card
    let resultCard = document.createElement("div");
    resultCard.classList.add("card");
    resultCard.classList.add("mb-1");
    resultCard.style.width = "99%";
	resultCard.setAttribute("onclick", "GoToArticle('./article.html?articleID=" + value.id + "');");
    searchResults.appendChild(resultCard);

    let hiddenThing = document.createElement("a");
    hiddenThing.href = "#";
    hiddenThing.classList.add("stretched-link");
    resultCard.appendChild(hiddenThing);

    let resultCardBody = document.createElement("div");
    resultCardBody.classList.add("card-body");
    resultCard.appendChild(resultCardBody);

    // Makale Adı
    let resultCardArticleName = document.createElement("h5");
    resultCardArticleName.classList.add("card-title");
    resultCardArticleName.classList.add("fw-bold");
    resultCardBody.appendChild(resultCardArticleName);

    let resultCardArticleNameText = document.createTextNode(value.yayinAd);
    resultCardArticleName.appendChild(resultCardArticleNameText);

    // Makale Linki
    let resultCardArticleLink = document.createElement("p");
    resultCardArticleLink.classList.add("card-subtitle");
    resultCardArticleLink.classList.add("mb-3");
    resultCardBody.appendChild(resultCardArticleLink);

    let resultCardArticleLinkText = document.createTextNode(value.urlAdresi);
    resultCardArticleLink.appendChild(resultCardArticleLinkText);

    // Yazarlar ve Yayımcı Adı
    let resultCardAuthors = document.createElement("p");
    resultCardAuthors.classList.add("card-text");
    resultCardAuthors.classList.add("fst-italic");
    resultCardBody.appendChild(resultCardAuthors);

    let authorsAndPublisherStr = value.yazarIsim;

    if(value.yayinciAdi != null)
    {
        authorsAndPublisherStr += " - " + value.yayinciAdi;
    }

    let resultCardAuthorsText = document.createTextNode(authorsAndPublisherStr);
    resultCardAuthors.appendChild(resultCardAuthorsText);

    // Özetin Özeti
    if(value.ozet != null)
    {
        let resultCardAbstract = document.createElement("p");
        resultCardAbstract.classList.add("card-text");
        resultCardBody.appendChild(resultCardAbstract);

        let resultCardAbstractText = document.createTextNode(value.ozet.slice(0, 200) + "...");
        resultCardAbstract.appendChild(resultCardAbstractText);
    }

    // Card Footer
    let resultCardFooter = document.createElement("div");
    resultCardFooter.classList.add("card-footer");
    resultCardFooter.classList.add("row");
    resultCard.appendChild(resultCardFooter);

    // Yayımlanma Tarihi
    let resultCardPublicationDate = document.createElement("span");
    resultCardPublicationDate.classList.add("card-link");
    resultCardPublicationDate.classList.add("col");
    resultCardFooter.appendChild(resultCardPublicationDate);

	let date = new Date(value.yayinTarih);
	let monthArr = [" Ocak ", " Şubat ", " Mart ", " Nisan ", " Mayıs ", " Haziran ", " Temmuz ", " Ağustos ", " Eylül ", " Ekim ", " Kasın ", " Aralık "];
    let resultCardPublicationDateText = document.createTextNode("Yayımlanma Tarihi: " + date.getDate() + monthArr[date.getMonth()] + date.getFullYear());
    resultCardPublicationDate.appendChild(resultCardPublicationDateText);

    // Alıntı Sayısı
    let resultCardCitations = document.createElement("span");
    resultCardCitations.classList.add("card-link");
    resultCardCitations.classList.add("col");
    resultCardFooter.appendChild(resultCardCitations);

    let resultCardCitationsText = document.createTextNode("Alıntı Sayısı: " + value.alintiSayisi);
    resultCardCitations.appendChild(resultCardCitationsText);

    // DOI Numarası
    if(value.doiNumarasi != null)
    {
        let resultCardDOI = document.createElement("span");
        resultCardDOI.classList.add("card-link");
        resultCardDOI.classList.add("col");
        resultCardFooter.appendChild(resultCardDOI);

        let resultCardDOIText = document.createTextNode("DOI Numarası: " + value.doiNumarasi);
        resultCardDOI.appendChild(resultCardDOIText);
    }
}

async function CheckIfReady()
{
    document.getElementById("mainPageButton").setAttribute("disabled", "");
    document.getElementById("searchButton").setAttribute("disabled", "");
    document.getElementById("searchText").setAttribute("readonly", "");

    document.getElementById("typoText").innerHTML = 'Arama yapılıyor&nbsp; <span class="font-monospace">' + waitingSymbols[waitingState] + '</span>';
    waitingState = (waitingState + 1) % 4;

    let response = await fetch("http://localhost:8080/part/" + searchID);
    let jsonRes = await response.json();

    document.getElementById("searchText").value = jsonRes.kelime;
    document.title = jsonRes.kelime + " - YILDIZsoft Akademik";
    autoPDF = jsonRes.autoPdf;

    if(jsonRes.hazir == true)
    {
        clearInterval(checkIfReadyInterval);
        foundArticles = jsonRes.foundArticleCount;
        console.log(jsonRes);

        document.getElementById("mainPageButton").removeAttribute("disabled");
        document.getElementById("searchButton").removeAttribute("disabled");
        document.getElementById("searchText").removeAttribute("readonly");

        if(foundArticles === 0)
        {
            document.getElementById("typoText").innerHTML = "Herhangi bir sonuç bulunamadı.";
        }
        else
        {
            document.getElementById("foundArticleCountText").innerHTML = foundArticles + " tane sonuç bulundu.";
            if(jsonRes.duzelenKelime.length > 0 && jsonRes.duzelenKelime !== jsonRes.kelime)
            {
                document.getElementById("typoText").classList.replace("text-center", "text-start");
                document.getElementById("typoText").innerHTML = "Yazımı düzeltilmiş sonuçları görüyorsunuz: " + jsonRes.duzelenKelime;
            }
            else
            {
                document.getElementById("typoText").classList.add("d-none");
            }
        }


        HandleSearchResults();
    }
}

async function SearchArticle()
{
	let searchKeywords = document.getElementById("searchText").value;
	console.log(searchKeywords);

	if(searchKeywords.length > 0)
	{
		let part = { kelime: searchKeywords, duzelenKelime: "", autoPdf: autoPDF, hazir: false, enableSerpAPI: enableSerbia, downloadPdfRequest: false, maxArticleCount: MAX_ARTICLE, foundArticleCount: 0 };
		let response = await fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
		let responseJson = await response.json();
		console.log(responseJson);

		window.location.assign("./search.html?searchID=" + responseJson.id);
	}
}

function GoToArticle(articleLink)
{
	window.open(articleLink, "_blank").focus();
}