let articleData = {};
//const tableRowClasses = "bg-transparent text-white align-middle";
const tableRowClasses = "align-middle";

async function onLoad()
{
	console.log("Makale sayfası yüklendi.");
	
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	const articleID = linkParameters.get("articleID");
	
	let response = await fetch("http://localhost:8080/veri/" + articleID);
	articleData = await response.json();
	
	console.log(articleData);
	
	document.title = articleData.yayinAd + " - KEFALsoft Akademik";
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

	response = await fetch("http://localhost:8080/referans?veriId=" + articleID);
    let referenceList = [];
	referenceList = await response.json();

	console.log(referenceList);
	referenceList.forEach(AddReferencesToList);
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

function OpenPDF()
{
    if(articleData.urlLink) window.open(articleData.urlLink, "_blank").focus();
}