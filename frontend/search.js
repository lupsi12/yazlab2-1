const MAX_ARTICLES = 10;

async function onLoad()
{
	const linkString = window.location.search;
	const linkParameters = new URLSearchParams(linkString);
	const searchID = linkParameters.get("searchID");
	
	let isHazir = false;
	
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
	}
	
	let articleListRes = await fetch("http://localhost:8080/veri");
	let articleList = await articleListRes.json();
	
	articleList = articleList.slice(Math.max(articleList.length - MAX_ARTICLES, 0));
	
	console.log(articleList);
	articleList.forEach(AddResultToList);
}

function AddResultToList(value, index, array)
{
	
}