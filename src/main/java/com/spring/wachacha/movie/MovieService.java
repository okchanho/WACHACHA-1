package com.spring.wachacha.movie;

import com.spring.wachacha.main.MainService;
import com.spring.wachacha.main.model.MovieSearchModel;
import com.spring.wachacha.movie.model.MovieEntity;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MovieService {

    private final MovieApiClient movieApiClient;
    @Autowired
    private MainService mainService;

    @Transactional(readOnly = true)
    public MovieEntity findByKeyword(String keyword){
        return movieApiClient.requestMovie(keyword);
    }
//test
    public MovieSearchModel info(String keyword){
        MovieSearchModel movieSearchModel = new MovieSearchModel();
        String responseBody = mainService.naverApi(keyword);
        JSONParser jParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jParser.parse(responseBody);
            MovieSearchModel t = new MovieSearchModel();
            t.setList((List<JSONObject>) jsonObject.get("items"));
            JSONObject jsonObject1 = t.getList().get(0);
            String link = (String) jsonObject1.get("link");
            Document doc = Jsoup.connect(link).get();
            String poster = doc.select("div.mv_info_area").select("img").attr("src");
            String name = doc.select("h3.h_movie").select("a").first().text();
            movieSearchModel.setName(name);
            movieSearchModel.setPoster(poster);
            System.out.println(link);
            Elements el = doc.select("ul.thumb_link_mv").select("li");
            Map<String, Object> summary = new HashMap<>();
            summary.put("title",doc.select("div.story_area").select("h5").text());
            summary.put("content", doc.select("div.story_area").select("p").text());
            movieSearchModel.setSummary(summary);
            Element info = doc.select("div.mv_info").get(1);
            movieSearchModel.setEngName(info.select("strong.h_movie2").text());
            Map<String, Object> spec = new HashMap<>();
            String[] spec_name = {"outline","director","appearance","rank","show"};
            Elements info_spec = info.select("dl.info_spec").select("dd");
            for(int i=0; i<info_spec.size(); i++){
                if(i == 2){
                    spec.put(spec_name[i],info_spec.get(i).text().substring(0,info_spec.get(i).text().length()-3));
                }else{
                    spec.put(spec_name[i],info_spec.get(i).text());
                }
            }
            String star = doc.select("div.netizen_score").select(".star_score").select("em").text();
            movieSearchModel.setSpec(spec);
            movieSearchModel.setStar(star);
            //bg_url 획득
            String bg_url = link.replace("basic","photoView");
            Document doc_url = Jsoup.connect(bg_url).get();
            movieSearchModel.setBg_url(doc_url.select(".viewer_img").select("img").attr("src"));

        } catch (Exception e){
            e.printStackTrace();
        }
        return movieSearchModel;
    }

    public Map<String, Object> Youtube(String keyword, String page){
        Map<String, Object> map = new HashMap<>();
        List<String> hrefList = new ArrayList();
        List<String> ImgList = new ArrayList();
        List<String> titleList = new ArrayList();
        List<String> writerList = new ArrayList();
        String url = "https://www.google.com/search?q="+keyword+"+%EC%9C%A0%ED%8A%9C%EB%B8%8C&tbm=vid&sxsrf=ALeKk01qBi-AgEWm7Jjh2ZXJ2uq6DpSdWA:1627012528071&ei=sD36YPf3A8OQr7wPiYG0oAk&start="+page+"&sa=N&ved=2ahUKEwj3j5_-pfjxAhVDyIsBHYkADZQQ8tMDegQIARBR&biw=1299&bih=787&dpr=1";
        try {
            Document doc = Jsoup.connect(url).get();
            Elements el = doc.select("a.rGhul");
            String[] cnt = ((doc.select("div.ij69rd.UHe5G")).text().split(" ")); //조회수
            for (Element e:el) {
                hrefList.add(e.attr("href"));
                Document doc2 = Jsoup.connect(e.attr("href")).get();
                ImgList.add(doc2.select("link[itemprop=thumbnailUrl]").attr("href")); //썸네일
                titleList.add(doc2.title().substring(0,doc2.title().length()-10)); //제목
                writerList.add(doc2.select("link[itemprop=name]").attr("content")); //작성자
            }
            map.put("hrefList",hrefList);
            map.put("ImgList",ImgList);
            map.put("titleList",titleList);
            map.put("writerList",writerList);
            map.put("cntList",cnt);
            map.put("page",page);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }
}
