package com.example.yazlab2_1;

import com.example.yazlab2_1.Entities.Part;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchAndScrapper implements Runnable
{
    public static int MAX_ARTICLES;
    public static boolean enableSerpAPI;
    public static final String serpAPIKey = "d4ac059996ec99c5af3591f253fefa66591ae5b8bc88326aacb379e535af4535";
    public static Part searchPart;

    public SearchAndScrapper(Part searchPart)
    {
        this.searchPart = searchPart;
    }

    public void run()
    {
        SearchAndScrap(this.searchPart);
    }

    public static void SearchAndScrap(Part searchThing)
    {
        searchPart = searchThing;

        String keyword = searchPart.getKelime();
        MAX_ARTICLES = searchPart.getMaxArticleCount();
        enableSerpAPI = searchPart.isEnableSerpAPI();

        try
        {
            if (searchPart.isDownloadPdfRequest())
            {
                String pdfUrl = searchPart.getKelime();
                String pdfName = searchPart.getDuzelenKelime();
                DownloadPDF(pdfUrl, pdfName);

                searchPart.setHazir(true);
                UpdatePart(searchPart);
            }
            else
            {
                String correctedKeyword = SpellCheck(keyword);
                searchPart.setDuzelenKelime(correctedKeyword);
                //UpdatePart(searchPart);

                ArrayList<SearchResult> articleResults = SearchArticles(correctedKeyword);
                ArrayList<ArticleStruct> articles = new ArrayList<>();

                int articleCount = articleResults.size();
                searchPart.setFoundArticleCount(articleCount);
                System.out.println("\n" + articleCount + " tane sonuç bulundu.");
                //UpdatePart(searchPart);

                for (SearchResult result : articleResults) {
                    System.out.println("\n[" + result.keyword + "] " + result.title + " - " + result.url);
                    ArticleStruct articleStruct = GetArticleData(result);
                    articles.add(articleStruct);
                    System.out.println(articleStruct);
                }

                if (searchPart.isAutoPdf()) {
                    for (ArticleStruct article : articles) {
                        DownloadPDF(article.pdfLink, article.yayinAd);
                    }
                }

                searchPart.setHazir(true);
                UpdatePart(searchPart);
            }
        }
        catch (Exception e)
        {
            System.err.println("Makale arama veya web scraping sırasında bir hata oluştu.");
            e.printStackTrace();
        }
    }

    public static void UpdatePart(Part part) throws IOException
    {
        HttpURLConnection dialup = (HttpURLConnection) new URL("http://localhost:8080/part/" + part.getId()).openConnection();
        dialup.setRequestMethod("PUT");
        dialup.setRequestProperty("Content-Type", "application/json");
        dialup.setRequestProperty("Accept", "application/json");
        dialup.setDoOutput(true);

        JSONObject partJson = new JSONObject();
        partJson.put("kelime", part.getKelime());
        partJson.put("duzelenKelime", part.getDuzelenKelime());
        partJson.put("autoPdf", part.isAutoPdf());
        partJson.put("hazir", part.isHazir());
        partJson.put("downloadPdfRequest", part.isDownloadPdfRequest());
        partJson.put("maxArticleCount", part.getMaxArticleCount());
        partJson.put("foundArticleCount", part.getFoundArticleCount());

        String partString = partJson.toString();

        OutputStream outputStream = dialup.getOutputStream();
        outputStream.write(partString.getBytes(StandardCharsets.UTF_8), 0, partString.getBytes(StandardCharsets.UTF_8).length);

        dialup.getResponseCode();
        dialup.disconnect();
    }

    public static int SaveArticleToDB(ArticleStruct article) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/veri").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        JSONObject articleJson = new JSONObject();
        articleJson.put("yayinAd", article.yayinAd);
        articleJson.put("yazarIsim", article.yazarIsim);
        articleJson.put("yayinTur", article.yayinTur);
        articleJson.put("yayinTarih", article.yayinTarih);
        articleJson.put("yayinciAdi", article.yayinciAdi);
        articleJson.put("aramaAnahtarKelime", article.aramaAnahtarKelime);
        articleJson.put("makaleAnahtarKelime", article.makaleAnahtarKelime);
        articleJson.put("ozet", article.ozet);
        articleJson.put("alintiSayisi", article.alintiSayisi);
        articleJson.put("doiNumarasi", article.doiNumarasi);
        articleJson.put("urlAdresi", article.urlAdresi);
        articleJson.put("urlLink", article.pdfLink);

        String jsonString = articleJson.toString();

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(jsonString.getBytes(StandardCharsets.UTF_8), 0, jsonString.getBytes(StandardCharsets.UTF_8).length);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder jsonBuilder = new StringBuilder();
        String responseString;

        while((responseString = bufferedReader.readLine()) != null)
        {
            jsonBuilder.append(responseString);
        }
        //System.out.println("Response from http://localhost:8080/veri:");
        //System.out.println(jsonBuilder);

        bufferedReader.close();
        outputStream.close();
        connection.disconnect();

        jsonString = jsonBuilder.toString();
        JSONObject jsonObject = new JSONObject(jsonString);

        return jsonObject.optInt("id");
    }

    public static void SaveReferenceToDB(String reference, int articleID) throws IOException
    {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/referans").openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("referans", reference);
        jsonObject.put("veri_id", articleID);

        //String referenceJson = "{\n\"referans\": \"" + reference + "\", \n\"veri_id\": " + articleID + "\n}";
        String referenceJson = jsonObject.toString();
        //System.out.println(referenceJson);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(referenceJson.getBytes(StandardCharsets.UTF_8), 0, referenceJson.getBytes(StandardCharsets.UTF_8).length);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder jsonBuilder = new StringBuilder();
        String responseString;

        while((responseString = bufferedReader.readLine()) != null)
        {
            jsonBuilder.append(responseString);
        }

        //System.out.println("Response from http://localhost:8080/referans:");
        //System.out.println(jsonBuilder);

        bufferedReader.close();
        outputStream.close();
        connection.disconnect();
    }

    public static void DeleteAllArticles() throws IOException
    {
        HttpURLConnection dialup = (HttpURLConnection) new URL("http://localhost:8080/referans").openConnection();
        dialup.setRequestMethod("DELETE");
        dialup.setDoOutput(true);
        dialup.getResponseCode();
        dialup.disconnect();

        dialup = (HttpURLConnection) new URL("http://localhost:8080/veri").openConnection();
        dialup.setRequestMethod("DELETE");
        dialup.setDoOutput(true);
        dialup.getResponseCode();
        dialup.disconnect();

        dialup = (HttpURLConnection) new URL("http://localhost:8080/sequence").openConnection();
        dialup.setRequestMethod("DELETE");
        dialup.setDoOutput(true);
        dialup.getResponseCode();
        dialup.disconnect();

        dialup = (HttpURLConnection) new URL("http://localhost:8080/part").openConnection();
        dialup.setRequestMethod("DELETE");
        dialup.setDoOutput(true);
        dialup.getResponseCode();
        dialup.disconnect();

        System.out.println("Tüm makaleler veritabanından silindi.");
    }

    public static String SpellCheck(String keyword) throws IOException
    {
        String correctSpell = keyword;

        if(enableSerpAPI)
        {
            String spellURL = "https://serpapi.com/search.html?engine=google_scholar&q=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) + "&api_key=";
            System.out.println("\nİmla kontrolü için bekleniyor - " + spellURL);

            HttpURLConnection dialup = (HttpURLConnection) new URL(spellURL + serpAPIKey).openConnection();
            int responseCode = dialup.getResponseCode();

            System.out.println("HTTP cevabı: " + responseCode);

            if(responseCode <= 309)
            {
                StringBuilder serpHtmlBuilder = new StringBuilder();
                String line;

                InputStream inputStream = dialup.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                while((line = bufferedReader.readLine()) != null)
                {
                    serpHtmlBuilder.append(line.strip());
                }

                bufferedReader.close();
                inputStream.close();

                //System.out.println("\nİmla kontrolü için alınan HTML:");
                //System.out.println(serpHtmlBuilder);

                String serpHtml = serpHtmlBuilder.toString();

                Document spellDocument = Jsoup.parse(serpHtml);
                Element spellBar = spellDocument.selectFirst("div#gs_res_ccl_top > div.gs_r > h2.gs_rt > a");

                if (spellBar != null)
                {
                    correctSpell = spellBar.text();
                    System.out.println("\n\"" + keyword + "\" yerine \"" + correctSpell + "\" aranıyor.");
                }
                //else System.out.println("\nİmla zımbırtısı bulunamadı.");
            }
            else
            {
                System.err.println("\n\"" + keyword + "\" için imla kontrolü başarısız oldu. HTTP cevabı: " + responseCode);
                if(responseCode == 429) System.err.println("SerpAPI tarafından sunulan API anahtarının geçerlilik süresi dolmuştur. \nPlanınızı yükselterek SerpAPI avantajlarından yararlanmaya devam edebilirsiniz.");
            }
            dialup.disconnect();
        }
        else
        {
            System.out.println("\nSerpAPI etkinleştirilmemiş. İmla kontrolü yapılmadan devam ediliyor.");
        }

        return correctSpell;
    }

    public static ArticleStruct UpdateArticleFromSerpia(ArticleStruct article) throws IOException
    {
        String authors = (article.yazarIsim != null) ? article.yazarIsim.replaceAll(",", "") : "";
        String siteName = new URL(article.urlAdresi).getHost();
        String citeURL = "https://serpapi.com/search.html?engine=google_scholar&q=" + URLEncoder.encode(article.yayinAd + " " + authors + " site:" + siteName, StandardCharsets.UTF_8) + "&api_key=";
        System.out.println("\nMakale güncellemesi için bekleniyor - " + citeURL);

        HttpURLConnection dialup = (HttpURLConnection) new URL(citeURL + serpAPIKey).openConnection();
        int responseCode = dialup.getResponseCode();

        System.out.println("HTTP cevabı: " + responseCode);

        if(responseCode <= 309)
        {
            StringBuilder serpHtmlBuilder = new StringBuilder();
            String line, serpHtmlString;

            InputStream inputStream = dialup.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            while((line = bufferedReader.readLine()) != null)
            {
                serpHtmlBuilder.append(line.strip());
            }

            serpHtmlString = serpHtmlBuilder.toString();

            bufferedReader.close();
            inputStream.close();

            Pattern publisherPattern = Pattern.compile("<div class=\"gs_a gs_fma_p\">.*?</div>(.*?)<span");
            Matcher publisherMatcher = publisherPattern.matcher(serpHtmlString);

            if(publisherMatcher.find())
            {
                article.yayinciAdi = publisherMatcher.group(1);
                System.out.println("Yeni yayıncı adı: " + article.yayinciAdi);
            }

            Document updateDocument = Jsoup.parse(serpHtmlString);

            Element citationElement = updateDocument.selectFirst("a[href^=/scholar?cites=]");
            if(citationElement != null)
            {
                String citationString = citationElement.text();

                Pattern pattern = Pattern.compile("([0-9]+)");
                Matcher matcher = pattern.matcher(citationString);

                if(matcher.find())
                {
                    int citationCount = Integer.parseInt(matcher.group(1));
                    System.out.println("Yeni alıntı sayısı: " + citationCount);
                    if(article.alintiSayisi < citationCount) article.alintiSayisi = citationCount;
                }
            }
        }
        else
        {
            System.err.println("Makale güncelleme başarısız oldu. HTTP cevabı: " + responseCode);
            if(responseCode == 429) System.err.println("SerpAPI tarafından sunulan API anahtarının geçerlilik süresi dolmuştur. \nPlanınızı yükselterek SerpAPI avantajlarından yararlanmaya devam edebilirsiniz.");
        }
        dialup.disconnect();

        return article;
    }

    public static ArrayList<SearchResult> SearchArticles(String keyword) throws IOException
    {
        ArrayList<SearchResult> searchResults = new ArrayList<>();

        if(MAX_ARTICLES <= 0) return searchResults;

        String searchURL = "https://dergipark.org.tr/tr/search?q=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8) + "&section=articles";
        System.out.println("\nArama sonuçları bekleniyor - " + searchURL);

        HttpURLConnection dialup = (HttpURLConnection) new URL(searchURL).openConnection();
        int responseCode = dialup.getResponseCode();
        System.out.println("HTTP cevabı: " + responseCode);

        InputStream inputStream = (responseCode <= 309) ? dialup.getInputStream() : dialup.getErrorStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        StringBuilder htmlBuilder = new StringBuilder();

        while((line = bufferedReader.readLine()) != null)
        {
            htmlBuilder.append(line.strip());
        }
        String htmlDocument = htmlBuilder.toString();

        bufferedReader.close();
        dialup.disconnect();

        Document document = Jsoup.parse(htmlDocument);
        Element articleCardGroup = document.selectFirst("div.article-cards");

        if(articleCardGroup != null)
        {
            Elements articleDatas = articleCardGroup.select("h5.card-title > a");

            int articleCount = 0;
            for(Element articleData : articleDatas)
            {
                if(articleCount >= MAX_ARTICLES) break;

                //System.out.println("Makale Adı: " + articleData.text());
                searchResults.add(new SearchResult(keyword, articleData.attr("href"), articleData.text()));
                articleCount++;
            }
        }
        else
        {
            System.out.println("Arama sonucu bulunamadı: " + keyword);
        }

        return searchResults;
    }

    public static ArticleStruct GetArticleData(SearchResult article) throws IOException, ParseException
    {
        ArticleStruct articleStruct = new ArticleStruct();
        articleStruct.aramaAnahtarKelime = article.keyword;
        articleStruct.urlAdresi = article.url;
        articleStruct.yayinAd = article.title;
        System.out.println("Makale sitesi bekleniyor - " + article.url);

        ArrayList<String> referenceList = new ArrayList<>();

        URL articleURL = new URL(article.url);
        HttpURLConnection dialup = (HttpURLConnection) articleURL.openConnection();
        int responseCode = dialup.getResponseCode();
        System.out.println("HTTP cevabı: " + responseCode);

        InputStream inputStream = (responseCode <= 309) ? dialup.getInputStream() : dialup.getErrorStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        boolean redirection = (responseCode >= 300 && responseCode <= 309);
        if(redirection) System.out.println("\nBaşka sayfaya yönlendiriliyor...");

        String line;
        StringBuilder htmlBuilder = new StringBuilder();

        while((line = bufferedReader.readLine()) != null)
        {
            htmlBuilder.append(line);
        }
        String htmlDocument = htmlBuilder.toString();

        bufferedReader.close();
        dialup.disconnect();

        Document document = Jsoup.parse(htmlDocument);

        if(redirection)
        {
            //System.out.println(document);
            Element redirectURL = document.selectFirst("a[href]");

            assert redirectURL != null;
            article.url = redirectURL.attr("href");

            return GetArticleData(article);
        }
        else
        {
            // Get PDF link
            Element pdfElement = document.selectFirst("a[href][title=\"Makale PDF linki\"]");
            if(pdfElement != null)
            {
                //System.out.println("pdfElement = " + pdfElement.select("a[href]"));
                articleStruct.pdfLink = articleURL.getProtocol() + "://" + articleURL.getHost() + pdfElement.attr("href");
            }

            // Get DOI number
            Element doiElement = document.selectFirst("a.doi-link, [href^=https://doi.org/]");
            if(doiElement != null)
            {
                //System.out.println("doiElement = " + doiElement.attr("abs:href"));
                String doiLink = doiElement.attr("href");
                Pattern pattern = Pattern.compile("https://doi.org/(.*?)/.*");
                Matcher matcher = pattern.matcher(doiLink);

                if(matcher.find())
                {
                    articleStruct.doiNumarasi = matcher.group(1);
                }
            }

            // Get citation count
            Element citeElement = document.selectFirst("a[href$=cited_by_articles]");
            if(citeElement != null)
            {
                String citeText = citeElement.text();
                Pattern pattern = Pattern.compile("([0-9]+)");
                Matcher matcher = pattern.matcher(citeText);

                if(matcher.find())
                {
                    //System.out.println("citeText = " + matcher.group(1));
                    articleStruct.alintiSayisi = Integer.parseInt(matcher.group(1));
                }
            }

            // Get abstract
            Elements abstractElements = document.select("div.active > div.article-abstract > p, div.active > div.article-abstract > span");
            for(Element abstractElement : abstractElements)
            {
                String abstractText = abstractElement.text();
                //System.out.println("abstractText = " + abstractText);
                if(!abstractText.isEmpty())
                {
                    articleStruct.ozet = abstractText;
                    break;
                }
            }

            // Get article keywords
            Element keywordElement = document.selectFirst("div.active > div.article-keywords > p");
            if(keywordElement != null)
            {
                articleStruct.makaleAnahtarKelime = keywordElement.text();
            }

            // Get references
            Element referenceElement = document.selectFirst("div.active > div.article-citations > div");
            if(referenceElement != null)
            {
                Elements references = referenceElement.select("ul > li");
                if(!references.isEmpty())
                {
                    for(Element ref : references)
                    {
                        referenceList.add(ref.ownText());
                    }
                }
                else referenceList.add(referenceElement.text());
            }

            // Get details
            Elements detailElements = document.select("table.record_properties, .table > tbody > tr");
            for(Element detailElement : detailElements)
            {
                if(detailElement == detailElements.first()) continue;

                if(detailElement.select("th").text().equals("Bölüm")) articleStruct.yayinTur = detailElement.select("td").text();
                else if(detailElement.select("th").text().equals("Yazarlar"))
                {
                    Elements authors = detailElement.select("td > p");
                    StringBuilder tempAuthor = new StringBuilder();

                    for(Element author : authors)
                    {
                        Element authorElement = author.selectFirst("p > a, p > span");
                        if(authorElement != null)
                        {
                            if(author == authors.first())
                                tempAuthor.append(authorElement.text());
                            else
                                tempAuthor.append(", " + authorElement.text());
                        }
                    }

                    articleStruct.yazarIsim = tempAuthor.toString();
                    articleStruct.yazarIsim = articleStruct.yazarIsim.replaceAll(" Bu kişi benim", "");
                }
                else if(detailElement.select("th").text().equals("Yayımlanma Tarihi"))
                {
                    articleStruct.yayinTarih = detailElement.select("td").text();

                    SimpleDateFormat dateFormatter = new SimpleDateFormat("d MMMM yyyy", new Locale("tr", "TR"));
                    Date date = dateFormatter.parse(articleStruct.yayinTarih);
                    //System.out.println("Yayımlanma tarihi = " + date);

                    articleStruct.yayinTarih = new SimpleDateFormat("yyyy-MM-dd").format(date);
                }
            }
        }

        if(enableSerpAPI) articleStruct = UpdateArticleFromSerpia(articleStruct);
        else System.out.println("\nSerpAPI etkinleştirilmemiş. Yayıncı adı ve alıntı sayısı güncellenmeden devam ediliyor.");

        int articleID = SaveArticleToDB(articleStruct);
        //System.out.println("Reference: " + articleStruct.references);
        //SaveReferenceToDB(articleStruct.references, articleID);
        for(String ref : referenceList)
        {
            SaveReferenceToDB(ref, articleID);
        }

        return articleStruct;
    }

    public static void DownloadPDF(String link, String filename) throws IOException, InterruptedException
    {
        if(link != null && !link.isEmpty())
        {
            File dir = new File("./pdf");
            if(!dir.exists()) dir.mkdir();

            filename = filename.replaceAll("[/\\\\:*?\"<>|]", " ");
            if(searchPart.isDownloadPdfRequest()) searchPart.setDuzelenKelime(filename);

            File pdfFile = new File("./pdf/" + filename + ".pdf");

            if(!pdfFile.exists())
            {
                System.out.println("PDF indiriliyor | " + filename);

                Thread.sleep(3000);
                URL url = new URL(link);
                ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());

                FileOutputStream fileOutputStream = new FileOutputStream("./pdf/" + filename + ".pdf");
                FileChannel fileChannel = fileOutputStream.getChannel();

                fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
                System.out.println("PDF indirildi   | " + filename);
            }
            else System.out.println("PDF zaten mevcut - " + filename + ".pdf");
        }
        else
        {
            if(searchPart.isDownloadPdfRequest()) searchPart.setDuzelenKelime(null);
            System.out.println(link + " adresindeki PDF bulunamadı. İlgili adres mevcut değil.");
        }
    }
}
