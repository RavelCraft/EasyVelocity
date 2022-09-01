package com.github.imdabigboss.easyvelocity.webserver.endpoints;

import com.github.imdabigboss.easyvelocity.EasyVelocity;
import com.github.imdabigboss.easyvelocity.utils.FileUtils;
import com.github.imdabigboss.easyvelocity.utils.XMLParser;
import com.github.imdabigboss.easyvelocity.webserver.EndpointType;
import com.github.imdabigboss.easyvelocity.webserver.PageRequest;
import com.github.imdabigboss.easyvelocity.webserver.PageReturn;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RHTMLEndpoint extends AbstractEndpoint {
    private final String[] pageBody;

    private RHTMLEndpoint(String lang, String title, String description, String path, String prebody, String body) {
        super(EndpointType.SIMPLE, lang.equals("en") ? path : path + lang);

        this.pageBody = FileUtils.resourceToString("web/base_" + lang + ".html")
                .replace("${header}", FileUtils.resourceToString("web/content/header.html"))
                .replace("${navbar}", FileUtils.resourceToString("web/content/navbar_" + lang + ".html"))
                .replace("${description}", description)
                .replace("${title}", title)
                .replace("${prebody}", prebody)
                .replace("${body}", body)
                .replace("${path}", path)
                .split("\n");
    }

    public static RHTMLEndpoint create(Path filePath) throws ParserConfigurationException, SAXException, IOException, IllegalStateException {
        if (Files.isDirectory(filePath)) {
            throw new IllegalStateException("MarkdownEndpoint cannot be of type ALL");
        }

        String rawDoc = Files.readString(filePath);

        String lang = XMLParser.getValue("rhtml.data.lang", rawDoc, "en");
        String title = XMLParser.getValue("rhtml.data.title", rawDoc, "No title");
        String description = XMLParser.getValue("rhtml.data.description", rawDoc, "No description");
        String path = XMLParser.getValue("rhtml.data.path", rawDoc, "/");

        String prebody = XMLParser.getValue("rhtml.prebody", rawDoc, "");
        String body = XMLParser.getValue("rhtml.body", rawDoc, "");

        if (!path.endsWith("/")) {
            path += "/";
        }

        return new RHTMLEndpoint(lang, title, description, path, prebody, body);
    }

    @Override
    protected PageReturn getPageContents(PageRequest request) {
        StringBuilder sb = new StringBuilder();
        for (String s : this.pageBody) {
            if (s.contains("${navbar-link}")) {
                String link = s.substring(s.indexOf("href=\"") + 6);
                link = link.substring(0, link.indexOf("\""));

                if (request.getPath().equals(link)) {
                    sb.append(s.replace("${navbar-link}", "class=\"active\""));
                } else {
                    sb.append(s.replace("${navbar-link}", ""));
                }
            } else if (s.contains("${posts_en}")) {
                sb.append(s.replace("${posts_en}", this.getPosts("en")));
            } else if (s.contains("${posts_fr}")) {
                sb.append(s.replace("${posts_fr}", this.getPosts("fr")));
            } else {
                sb.append(s);
            }
        }

        return new PageReturn(sb.toString());
    }

    private String getPosts(String lang) {
        try {
            Path folder = Paths.get("/var/www/db/ravelposts/post" + lang);
            int postNum = (int) Files.list(folder).count();

            StringBuilder sb = new StringBuilder();
            for (int i = postNum; i > Math.max(postNum - 20, 1); i--) {
                String[] contents = Files.readString(folder.resolve("post_" + i + ".txt")).split("\\|\\|\\|");

                sb.append("<div class='post-container'><b><span class='post-sender'>").append(contents[0]).append("</span></b><br><p style='font-size: 20px;float: center;'>").append(contents[1].replace("\n", "<br />\n")).append("</p><br><b><span class='post-time'>").append(contents[2]).append(" UTC</span></b><br/></div><br />");
            }

            return sb.toString();
        } catch (IOException e) {
            EasyVelocity.getLogger().error("Error while getting posts", e);
        }

        return "<div class='post-container'><p style='font-size: 20px;float: center;'>:(</p><br /></div><br />";
    }
}