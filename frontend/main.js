const MAX_ARTICLE = 10;
const enableSerbia = true;

let articleArray = [];
//const tableRowClasses = "bg-transparent text-white align-middle";
const tableRowClasses = "align-middle";

let namesAscending = false;
let datesAscending = false;
let citesAscending = false;
let autoPDF = true;

async function onLoad()
{
	console.log("Anasayfa yüklendi.");
	
	document.getElementById("searchText").value = "";
	document.getElementById("filterText").value = "";
	
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	if(linkParameters.has("dev_cmd"))
	{
		const devMode = linkParameters.get("dev_cmd");
		if(devMode === "DROPTABLES")
		{
			if(window.confirm("Veritabanındaki tüm makaleleri gerçekten silmek istiyor musunuz? Bu işlem geri alınamaz!") == true)
			{
				await fetch("http://localhost:8080/veri", { method: "DELETE", headers: { "Content-Type": "application/json" }});
				await fetch("http://localhost:8080/referans", { method: "DELETE", headers: { "Content-Type": "application/json" }});
				await fetch("http://localhost:8080/part", { method: "DELETE", headers: { "Content-Type": "application/json" }});
				await fetch("http://localhost:8080/sequence", { method: "DELETE", headers: { "Content-Type": "application/json" }});
				
				window.alert("Veritabanındaki tüm makaleler başarıyla silindi.");
				window.location.assign("./main.html");
			}
		}
	}
	
	let response = await fetch("http://localhost:8080/veri");
	articleArray = await response.json();
	//articleArray = [];
	
	if(articleArray.length > 0)
	{
		console.log(articleArray);
		InvertNameSort();
		document.getElementById("articlesInDatabaseTitle").innerHTML = "Önceden Aranmış Makaleler - " + articleArray.length + " Adet";
		document.getElementById("savedArticles").classList.remove("d-none");
		document.getElementById("articleNotFoundText").classList.add("d-none");
	}
	else
	{
		document.getElementById("savedArticles").classList.add("d-none");
		document.getElementById("articleNotFoundText").classList.remove("d-none");
	}
}

function RemoveDuplicateArticles(articles)
{
	/*let uniqueArticles = [];
	
	for(let i = 0; i < articles.length; i++)
	{
		if(uniqueArticles.length == 0) uniqueArticles.push(articles[i]);
		if()
	}
	
	return uniqueArticles;*/
}

function SearchInArticles()
{
	let filters = document.getElementById("filterText").value;
	let filteredArticles = [];
	
	for(let i = 0; i < articleArray.length; i++)
	{
		if(	articleArray[i].yayinAd.includes(filters) ||
			articleArray[i].yazarIsim.includes(filters) ||
			articleArray[i].yayinTur.includes(filters) ||
			articleArray[i].yayinTarih.includes(filters) ||
			articleArray[i].yayinciAdi.includes(filters) ||
			articleArray[i].aramaAnahtarKelime.includes(filters) ||
			articleArray[i].makaleAnahtarKelime.includes(filters) ||
			articleArray[i].alintiSayisi == filters ||
			(articleArray[i].doiNumarasi != null && articleArray[i].doiNumarasi.includes(filters)) ||
			articleArray[i].ozet.includes(filters) ||
			(articleArray[i].urlAdresi != null && articleArray[i].urlAdresi.includes(filters)) ||
			(articleArray[i].urlLink != null && articleArray[i].urlLink.includes(filters)))
		{
			filteredArticles.push(articleArray[i]);
		}
	}
	
	DropTable();
	filteredArticles.forEach(AddArticleToArticleTable);
}

function ToggleAutoPDF()
{
	autoPDF = !autoPDF;
	
	if(autoPDF) document.getElementById("autoPdfButton").classList.replace("btn-outline-light", "btn-light");
	else        document.getElementById("autoPdfButton").classList.replace("btn-light", "btn-outline-light");
}

async function SearchArticle()
{
	let searchKeywords = document.getElementById("searchText").value;
	console.log(searchKeywords);
	
	if(searchKeywords.length > 0)
	{
		let part = { kelime: searchKeywords, duzelenKelime: "", autoPdf: autoPDF, hazir: false, enableSerpAPI: enableSerbia, maxArticleCount: MAX_ARTICLE, foundArticleCount: 0 };
		let response = await fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
		let responseJson = await response.json();
		console.log(responseJson);

		//fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
		//setTimeout(() => { console.log(""); }, 1000);

		window.location.assign("./search.html?searchID=" + responseJson.id);
		//window.location.assign("./search.html");
	}
}

function InvertNameSort()
{
	namesAscending = !namesAscending;
	citesAscending = false;
	datesAscending = false;
	
	document.getElementById("tableArticleName").innerHTML = "Makale Adı " + ((namesAscending == true) ? "/\\" : "\\/");
	//document.getElementById("tableCitation").innerHTML = "Alıntı Sayısı";
	document.getElementById("tableCitation").innerHTML = "Alıntılar";
	document.getElementById("tableArticleDate").innerHTML = "Yayımlanma Tarihi";
	
	articleArray = SortArticlesByName(articleArray);
	DropTable();
	articleArray.forEach(AddArticleToArticleTable);
}

function InvertCiteSort()
{
	citesAscending = !citesAscending;
	namesAscending = false;
	datesAscending = false;
	
	//document.getElementById("tableCitation").innerHTML = "Alıntı Sayısı " + ((citesAscending == true) ? "/\\" : "\\/");
	document.getElementById("tableCitation").innerHTML = "Alıntılar " + ((citesAscending == true) ? "/\\" : "\\/");
	document.getElementById("tableArticleName").innerHTML = "Makale Adı";
	document.getElementById("tableArticleDate").innerHTML = "Yayımlanma Tarihi";
	
	articleArray = SortArticlesByCite(articleArray);
	DropTable();
	articleArray.forEach(AddArticleToArticleTable);
}

function InvertDateSort()
{
	datesAscending = !datesAscending;
	namesAscending = false;
	citesAscending = false;
	
	document.getElementById("tableArticleDate").innerHTML = "Yayımlanma Tarihi " + ((datesAscending == true) ? "/\\" : "\\/");
	document.getElementById("tableArticleName").innerHTML = "Makale Adı";
	//document.getElementById("tableCitation").innerHTML = "Alıntı Sayısı";
	document.getElementById("tableCitation").innerHTML = "Alıntılar";
	
	articleArray = SortArticlesByDate(articleArray);
	DropTable();
	articleArray.forEach(AddArticleToArticleTable);
}

function SortArticlesByDate(articles)
{
	for(let i = 0; i < articles.length - 1; i++)
	{
		for(let j = i + 1; j < articles.length; j++)
		{
			let date1 = new Date(articles[i].yayinTarih);
			let date2 = new Date(articles[j].yayinTarih);
				
			if(datesAscending)
			{
				if(date1.getTime() > date2.getTime())
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
			else
			{
				if(date1.getTime() < date2.getTime())
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
		}
	}
	
	return articles;
}

function SortArticlesByCite(articles)
{
	for(let i = 0; i < articles.length - 1; i++)
	{
		for(let j = i + 1; j < articles.length; j++)
		{
			if(citesAscending)
			{
				if(articles[i].alintiSayisi > articles[j].alintiSayisi)
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
			else
			{
				if(articles[i].alintiSayisi < articles[j].alintiSayisi)
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
		}
	}
	
	return articles;
}

function SortArticlesByName(articles)
{
	for(let i = 0; i < articles.length - 1; i++)
	{
		for(let j = i + 1; j < articles.length; j++)
		{
			if(namesAscending)
			{
				if(articles[i].yayinAd.localeCompare(articles[j].yayinAd) > 0)
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
			else
			{
				if(articles[i].yayinAd.localeCompare(articles[j].yayinAd) < 0)
				{
					let temp = articles[i];
					articles[i] = articles[j];
					articles[j] = temp;
				}
			}
		}
	}
	
	return articles;
}

function GoToArticle(articleLink)
{
	window.open(articleLink, "_blank").focus();
}

function DropTable()
{
	document.getElementById("articleTableBody").innerHTML = "";
}

function AddArticleToArticleTable(value, index, array)
{
	let liste = document.getElementById("articleTableBody");
	let satir = liste.insertRow(-1);
	satir.setAttribute("onclick", "GoToArticle('./article.html?articleID=" + value.id + "');");
	
	// Makale adı
	let articleName = satir.insertCell(-1);
	articleName.className = tableRowClasses;
	let yazi = document.createTextNode(value.yayinAd);
	articleName.appendChild(yazi);
	
	// Yazarlar
	let authors = satir.insertCell(-1);
	authors.className = tableRowClasses;
	yazi = document.createTextNode(value.yazarIsim);
	authors.appendChild(yazi);
	
	// Yayın türü
	let articleType = satir.insertCell(-1);
	articleType.className = tableRowClasses;
	yazi = document.createTextNode(value.yayinTur);
	articleType.appendChild(yazi);
	
	// Yayın tarihi
	let publicationDate = satir.insertCell(-1);
	publicationDate.className = tableRowClasses;
	let date = new Date(value.yayinTarih);
	let monthArr = [" Ocak ", " Şubat ", " Mart ", " Nisan ", " Mayıs ", " Haziran ", " Temmuz ", " Ağustos ", " Eylül ", " Ekim ", " Kasın ", " Aralık "];
	yazi = document.createTextNode(date.getDate() + monthArr[date.getMonth()] + date.getFullYear());
	publicationDate.appendChild(yazi);
	
	// Yayıncı adı
	let publisher = satir.insertCell(-1);
	publisher.className = tableRowClasses;
	yazi = document.createTextNode((value.yayinciAdi == null) ? "Yok" : value.yayinciAdi);
	publisher.appendChild(yazi);
	
	// Arama keywords
	let searchKeywords = satir.insertCell(-1);
	searchKeywords.className = tableRowClasses;
	yazi = document.createTextNode(value.aramaAnahtarKelime);
	searchKeywords.appendChild(yazi);
	
	// Makale keywords
	let articleKeywords = satir.insertCell(-1);
	articleKeywords.className = tableRowClasses;
	yazi = document.createTextNode(value.makaleAnahtarKelime);
	articleKeywords.appendChild(yazi);
	
	// Özet
	/*let tldr = satir.insertCell(-1);
	tldr.className = tableRowClasses;
	yazi = document.createTextNode(value.ozet);
	tldr.appendChild(yazi);*/
	
	// Alıntı sayısı
	let citations = satir.insertCell(-1);
	citations.className = tableRowClasses;
	yazi = document.createTextNode(value.alintiSayisi);
	citations.appendChild(yazi);
	
	// DOI numarası
	let doi = satir.insertCell(-1);
	doi.className = tableRowClasses;
	yazi = document.createTextNode((value.doiNumarasi == null) ? "Yok" : value.doiNumarasi);
	doi.appendChild(yazi);
	
	// Makale linki
	/*let articleLink = satir.insertCell(-1);
	articleLink.className = tableRowClasses;
	if(value.urlAdresi != null)
	{
		let a = document.createElement('a');
		let url = document.createTextNode(value.urlAdresi);
		a.appendChild(url);
		a.title = value.urlAdresi;
		a.href = value.urlAdresi;
		//a.classList.add("link-success");
		articleLink.appendChild(a);
	}
	
	// PDF linki
	let pdfLink = satir.insertCell(-1);
	pdfLink.className = tableRowClasses;
	if(value.urlLink != null)
	{
		let a = document.createElement('a');
		let url = document.createTextNode(value.urlLink);
		a.appendChild(url);
		a.title = value.urlLink;
		a.href = value.urlLink;
		//a.classList.add("link-success");
		articleLink.appendChild(a);
		pdfLink.appendChild(a);
	}*/
}