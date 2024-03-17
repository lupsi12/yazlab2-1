const MAX_ARTICLES = 10;
let checkIfReadyInterval;
//let searchResults = [];
let searchID;

const waitingSymbols = ["-", "\\", "|", "/"];
let waitingState = 0;

async function onLoad()
{
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	searchID = linkParameters.get("searchID");
	
	/*let isHazir = false;
	
	while(!isHazir)
	{
		let response = await fetch("http://localhost:8080/part/" + searchID);
		let jsonRes = await response.json();
		
		document.getElementById("searchText").value = jsonRes.kelime;
		
		if(jsonRes.duzelenKelime.length > 0 && jsonRes.duzelenKelime !== jsonRes.kelime)
		{
			document.getElementById("typoText").innerHTML = "Yazımı düzeltilmiş sonuçları görüyorsunuz: " + jsonRes.duzelenKelime;
		}
		
		isHazir = jsonRes.hazir;
	}*/
	checkIfReadyInterval = setInterval(CheckIfReady, 250);
	
	/*let articleListRes = await fetch("http://localhost:8080/veri");
	let articleList = await articleListRes.json();

	articleList = articleList.slice(Math.max(articleList.length - MAX_ARTICLES, 0));

	console.log(articleList);
	articleList.forEach(AddResultToList);*/
}

async function HandleSearchResults()
{
    let articleListRes = await fetch("http://localhost:8080/veri");
    let articleList = await articleListRes.json();

    articleList = articleList.slice(Math.max(articleList.length - MAX_ARTICLES, 0));

    console.log(articleList);
    articleList.forEach(AddResultToList);
}

function AddResultToList(value, index, array)
{
	
}

async function CheckIfReady()
{
    document.getElementById("typoText").innerHTML = 'Arama yapılıyor&nbsp; <span class="font-monospace">' + waitingSymbols[waitingState] + '</span>';
    waitingState = (waitingState + 1) % 4;

    let response = await fetch("http://localhost:8080/part/" + searchID);
    let jsonRes = await response.json();
    //let response = await fetch("http://localhost:8080/part");
    //let searchResults = await response.json();
    //let jsonRes = searchResults[-1];

    //console.log(jsonRes);
    document.getElementById("searchText").value = jsonRes.kelime;

    if(!jsonRes.hazir) return;

    clearInterval(checkIfReadyInterval);

    if(jsonRes.duzelenKelime.length > 0 && jsonRes.duzelenKelime !== jsonRes.kelime)
    {
    	document.getElementById("typoText").innerHTML = "Yazımı düzeltilmiş sonuçları görüyorsunuz: " + jsonRes.duzelenKelime;
    }
    else
    {
    	document.getElementById("typoText").innerHTML = "Sonuçlar gösteriliyor: " + jsonRes.kelime;
    }

    HandleSearchResults();
}