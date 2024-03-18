let articleData = {};
//const tableRowClasses = "bg-transparent text-white align-middle";
const tableRowClasses = "align-middle";

let checkIfDownloadedInterval;
let pdfID;
let pdfName;

const waitingSymbols = ["-", "\\", "|", "/"];
let waitingState = 0;

async function onLoad()
{
	console.log("Makale sayfası yüklendi.");
	
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	const articleID = linkParameters.get("articleID");
	
	let response = await fetch("http://localhost:8080/veri/" + articleID);
	articleData = await response.json();
	
	console.log(articleData);
	
	document.title = articleData.yayinAd + " - YILDIZsoft Akademik";
	document.getElementById("articleName").innerHTML = articleData.yayinAd;
	document.getElementById("authorsText").innerHTML = articleData.yazarIsim;
	document.getElementById("articleTypeText").innerHTML = articleData.yayinTur;

	let date = new Date(articleData.yayinTarih);
	let monthArr = [" Ocak ", " Şubat ", " Mart ", " Nisan ", " Mayıs ", " Haziran ", " Temmuz ", " Ağustos ", " Eylül ", " Ekim ", " Kasın ", " Aralık "];
	document.getElementById("publicationDateText").innerHTML = date.getDate() + monthArr[date.getMonth()] + date.getFullYear();

	document.getElementById("publisherText").innerHTML = (articleData.yayinciAdi == null) ? "Yok" : articleData.yayinciAdi;
	document.getElementById("searchKeywordsText").innerHTML = articleData.aramaAnahtarKelime;
	document.getElementById("articleKeywordsText").innerHTML = articleData.makaleAnahtarKelime;
	document.getElementById("citationsText").innerHTML = articleData.alintiSayisi;
	document.getElementById("doiText").innerHTML = (articleData.doiNumarasi == null) ? "Yok" : articleData.doiNumarasi;

	document.getElementById("abstractText").innerHTML = (articleData.ozet == null) ? "Yok" : articleData.ozet;
    /*response = await fetch(pdfLink);
    if(response.ok)
    {
        document.getElementById("openPdfButton").innerHTML = 'PDF <span class="fa">&#xf08e;</span>';
    }
    else
    {
        document.getElementById("openPdfButton").innerHTML = 'PDF İndir';
    }*/

	response = await fetch("http://localhost:8080/referans?veriId=" + articleID);
    let referenceList = [];
	referenceList = await response.json();

	console.log(referenceList);
	referenceList.forEach(AddReferencesToList);

	let part = { kelime: articleData.urlLink, duzelenKelime: articleData.yayinAd, autoPdf: false, hazir: false, enableSerpAPI: false, downloadPdfRequest: true, maxArticleCount: 0, foundArticleCount: 0 };
	response = await fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
	let responseJson = await response.json();
	console.log(responseJson);

	pdfID = responseJson.id;

	checkIfDownloadedInterval = setInterval(CheckIfPdfDownloaded, 250);
}

function AddReferencesToList(value, index, array)
{
    let references = document.getElementById("referenceList");

    let referenceCard = document.createElement("div");
    referenceCard.classList.add("card");
    referenceCard.classList.add("mb-1");
    references.appendChild(referenceCard);

    let referenceCardBody = document.createElement("div");
    referenceCardBody.classList.add("card-body");
    referenceCard.appendChild(referenceCardBody);

    let referenceCardBodyText = document.createTextNode(value.referans);
    referenceCardBody.appendChild(referenceCardBodyText);
}

function GoToArticle()
{
    window.open(articleData.urlAdresi, "_blank").focus();
}

async function OpenPDF()
{
    let pdfLink = "../pdf/" + pdfName + ".pdf";
    window.open(pdfLink, "_blank").focus();

    /*let response = await fetch(pdfLink);
    if(response.ok)
    {
        window.open(pdfLink, "_blank").focus();
    }
    else if(articleData.urlLink)
    {
        let pdfButton = document.getElementById("openPdfButton");
        pdfButton.setAttribute("disabled", "");
        pdfButton.innerHTML = "PDF İndiriliyor";

        checkIfDownloadedInterval = setInterval(CheckIfPdfDownloaded, 250);
    }*/
    /*else if(articleData.urlLink)
    {
        window.open(articleData.urlLink, "_blank").focus();
    }*/
}

async function CheckIfPdfDownloaded()
{
    document.getElementById("openPdfButton").innerHTML = 'PDF İndiriliyor&nbsp; <span class="font-monospace">' + waitingSymbols[waitingState] + '</span>';
    waitingState = (waitingState + 1) % 4;
    document.getElementById("openPdfButton").setAttribute("disabled", "");

	/*let part = { kelime: articleData.urlLink, duzelenKelime: articleData.yayinAd, autoPdf: autoPDF, hazir: false, enableSerpAPI: enableSerbia, downloadPdfRequest: true, maxArticleCount: MAX_ARTICLE, foundArticleCount: 0 };
	let response = await fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
	let responseJson = await response.json();
	console.log(responseJson);*/

	let response = await fetch("http://localhost:8080/part/" + pdfID);
	let responseJson = await response.json();

	if(responseJson.hazir)
	{
	    clearInterval(checkIfDownloadedInterval);

        pdfName = responseJson.duzelenKelime;
	    document.getElementById("openPdfButton").removeAttribute("disabled");
        document.getElementById("openPdfButton").innerHTML = 'PDF <span class="fa">&#xf08e;</span>';
	    //OpenPDF();
	}
}