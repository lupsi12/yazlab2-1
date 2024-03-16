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
	
	document.title = articleData.yayinAd;
	document.getElementById("articleName").innerHTML = articleData.yayinAd;
}