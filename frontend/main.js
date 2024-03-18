const MAX_ARTICLE = 10;
const enableSerbia = true;

let articleArray = [];
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
		articleArray = RemoveDuplicateArticles(articleArray);
		InvertNameSort();
		document.getElementById("articlesInDatabaseTitle").innerHTML = "Kayıtlı Makaleler - " + articleArray.length + " Adet";
		document.getElementById("savedArticles").classList.remove("d-none");
		document.getElementById("articleNotFoundText").classList.add("d-none");
	}
	else
	{
		document.getElementById("savedArticles").classList.add("d-none");
		document.getElementById("articleNotFoundText").classList.remove("d-none");
	}
}

async function ClearAllFilters()
{
    document.getElementById("filterText").value = "";
    document.getElementById("filterArticleName").value = "";
    document.getElementById("filterAuthors").value = "";
    document.getElementById("filterArticleType").value = "";
    document.getElementById("filterPublicationDate").value = "";
    document.getElementById("filterPublisher").value = "";
    document.getElementById("filterSearchKeywords").value = "";
    document.getElementById("filterArticleKeywords").value = "";
    document.getElementById("filterCitations").value = "";
    document.getElementById("filterDoi").value = "";

	let response = await fetch("http://localhost:8080/veri");
	articleArray = await response.json();

	articleArray = RemoveDuplicateArticles(articleArray);
	namesAscending = false;
	InvertNameSort();

	document.getElementById("articlesInDatabaseTitle").innerHTML = "Kayıtlı Makaleler - " + articleArray.length + " Adet";
	if(articleArray.length === 0)
	{
	    document.getElementById("articleTable").classList.add("d-none");
		document.getElementById("articleNotFoundText").classList.remove("d-none");
	}
	else
	{
	    document.getElementById("articleTable").classList.remove("d-none");
		document.getElementById("articleNotFoundText").classList.add("d-none");
	}

    //DropTable();
    //articleArray.forEach(AddArticleToArticleTable);
}

function RemoveDuplicateArticles(articles)
{
	let uniqueArticles = [];
	
	for(let a of articles)
	{
		if(uniqueArticles.length == 0) uniqueArticles.push(a);
		else
		{
		    let add = true;
		    for(let u of uniqueArticles)
		    {
		        if(a.urlAdresi === u.urlAdresi) add = false;
		    }

		    if(add) uniqueArticles.push(a);
		}
	}
	
	return uniqueArticles;
}

async function SearchInArticles()
{
	let filters = document.getElementById("filterText").value.toLowerCase();

	let filterArticleName = document.getElementById("filterArticleName").value.toLowerCase();
	let filterAuthors = document.getElementById("filterAuthors").value.toLowerCase();
	let filterArticleType = document.getElementById("filterArticleType").value.toLowerCase();
	let filterPublicationDate = document.getElementById("filterPublicationDate").value.toLowerCase();
	let filterPublisher = document.getElementById("filterPublisher").value.toLowerCase();
	let filterSearchKeywords = document.getElementById("filterSearchKeywords").value.toLowerCase();
	let filterArticleKeywords = document.getElementById("filterArticleKeywords").value.toLowerCase();
	let filterCitations = document.getElementById("filterCitations").value.toLowerCase();
	let filterDoi = document.getElementById("filterDoi").value.toLowerCase();

	let response = await fetch("http://localhost:8080/veri");
	articleArray = await response.json();

	articleArray = RemoveDuplicateArticles(articleArray);
	namesAscending = false;
	InvertNameSort();

	let filteredArticles = [];

	for(let a of articleArray)
	{
	    filteredArticles.push(a);
	}

	//console.log("filteredArticles = " + filteredArticles);

	if(filters.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        //console.log(a);
	        if(!(a.yayinAd.toLowerCase().includes(filters) ||
	             a.yazarIsim.toLowerCase().includes(filters) ||
	             a.yayinTur.toLowerCase().includes(filters) ||
	             //a.yayinTarih.toLowerCase().includes(filters) ||
	             //a.yayinciAdi.toLowerCase().includes(filters) ||
	             a.aramaAnahtarKelime.toLowerCase().includes(filters) ||
	             //a.makaleAnahtarKelime.toLowerCase().includes(filters) ||
	             a.alintiSayisi.toString().includes(filters) ||
	             //a.doiNumarasi.toLowerCase().includes(filters) ||
	             //a.ozet.toLowerCase().includes(filters) ||
	             a.urlAdresi.toLowerCase().includes(filters)
	             //a.urlLink.toLowerCase().includes(filters)
	        ))
	        {
	            let toRemove = true;

                if(a.yayinciAdi != null && a.yayinciAdi.toLowerCase().includes(filters))
	            {
	                toRemove = false;
	                //delete filteredArticles[filteredArticles.indexOf(a)];
	            }
	            if(a.makaleAnahtarKelime != null && a.makaleAnahtarKelime.toLowerCase().includes(filters))
	            {
	                toRemove = false;
                    //delete filteredArticles[filteredArticles.indexOf(a)];
	            }
	            if(a.doiNumarasi != null && a.doiNumarasi.toLowerCase().includes(filters))
	            {
	                toRemove = false;
	                //delete filteredArticles[filteredArticles.indexOf(a)];
	            }
	            if(a.ozet != null && a.ozet.toLowerCase().includes(filters))
	            {
	                toRemove = false;
	                //delete filteredArticles[filteredArticles.indexOf(a)];
	            }
	            if(a.urlLink != null && a.urlLink.toLowerCase().includes(filters))
	            {
	                toRemove = false;
	                //delete filteredArticles[filteredArticles.indexOf(a)];
	            }

	            //console.log("Yayımlanma tarihi: " + a.yayinTarih);
                let tempDate = new Date(a.yayinTarih);
                let monthArr = [" Ocak ", " Şubat ", " Mart ", " Nisan ", " Mayıs ", " Haziran ", " Temmuz ", " Ağustos ", " Eylül ", " Ekim ", " Kasın ", " Aralık "];
                let dateText = tempDate.getDate() + monthArr[tempDate.getMonth()] + tempDate.getFullYear();
                //console.log("Yayımlanma tarihi: " + dateText);
                //console.log("filters: " + filters);
                //console.log("includes: " + dateText.toLowerCase().includes(filters));
                if(dateText.toLowerCase().includes(filters))
                {
	                toRemove = false;
                    //delete filteredArticles[filteredArticles.indexOf(a)];
                }

                if(toRemove) delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return element !== undefined;
	});

    // Makale adı
	if(filterArticleName.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(!a.yayinAd.toLowerCase().includes(filterArticleName))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Yazarlar
	if(filterAuthors.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(!a.yazarIsim.toLowerCase().includes(filterAuthors))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Makale türü
	if(filterArticleType.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(!a.yayinTur.toLowerCase().includes(filterArticleType))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Yayımlanma tarihi
	if(filterPublicationDate.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        /*if(!a.yayinTarih.toLowerCase().includes(filterPublicationDate))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }*/
	        let tempDate = new Date(a.yayinTarih);
            let monthArr = [" Ocak ", " Şubat ", " Mart ", " Nisan ", " Mayıs ", " Haziran ", " Temmuz ", " Ağustos ", " Eylül ", " Ekim ", " Kasın ", " Aralık "];
            let dateText = tempDate.getDate() + monthArr[tempDate.getMonth()] + tempDate.getFullYear();
                console.log("Yayımlanma tarihi: " + dateText);
                console.log("filterPublicationDate: " + filterPublicationDate);
                console.log("includes: " + dateText.toLowerCase().includes(filterPublicationDate));
            if(!dateText.toLowerCase().includes(filterPublicationDate))
            {
                delete filteredArticles[filteredArticles.indexOf(a)];
            }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Yayıncı adı
	if(filterPublisher.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(a.yayinciAdi != null && !a.yayinciAdi.toLowerCase().includes(filterPublisher))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Arama kelimeleri
	if(filterSearchKeywords.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(!a.aramaAnahtarKelime.toLowerCase().includes(filterSearchKeywords))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Makale anahtar kelimeleri
	if(filterArticleKeywords.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(a.makaleAnahtarKelime != null && !a.makaleAnahtarKelime.toLowerCase().includes(filterArticleKeywords))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // Alıntılar
	if(filterCitations.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(!a.alintiSayisi.toString().includes(filterCitations))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

    // DOI numarası
	if(filterDoi.length > 0)
	{
	    for(let a of filteredArticles)
	    {
	        if(a.doiNumarasi != null && !a.doiNumarasi.toLowerCase().includes(filterDoi))
	        {
	            delete filteredArticles[filteredArticles.indexOf(a)];
	        }
	    }
	}

	filteredArticles = filteredArticles.filter(function(element) {
	    return !!element;
	});

	articleArray = [];
	for(let f of filteredArticles) articleArray.push(f);
	
	DropTable();
	articleArray.forEach(AddArticleToArticleTable);

	document.getElementById("articlesInDatabaseTitle").innerHTML = "Filtrelenmiş Makaleler - " + articleArray.length + " Adet";
	if(articleArray.length === 0)
	{
	    document.getElementById("articleTable").classList.add("d-none");
		document.getElementById("articleNotFoundText").classList.remove("d-none");
	}
	else
	{
	    document.getElementById("articleTable").classList.remove("d-none");
		document.getElementById("articleNotFoundText").classList.add("d-none");
	}
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
		let part = { kelime: searchKeywords, duzelenKelime: "", autoPdf: autoPDF, hazir: false, enableSerpAPI: enableSerbia, downloadPdfRequest: false, maxArticleCount: MAX_ARTICLE, foundArticleCount: 0 };
		let response = await fetch("http://localhost:8080/part", { method: "POST", headers: { "Content-Type": "application/json" }, body: JSON.stringify(part) });
		let responseJson = await response.json();
		console.log(responseJson);

		window.location.assign("./search.html?searchID=" + responseJson.id);
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
}