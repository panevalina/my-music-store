package mk.ukim.finki.wp.lab.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mk.ukim.finki.wp.lab.service.implementation.ArtistServiceImpl;
import mk.ukim.finki.wp.lab.service.implementation.SongServiceImpl;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(name = "details",urlPatterns = "/showDetails" )
public class SongDetailsServlet extends HttpServlet {
    private final SpringTemplateEngine springTemplateEngine;
    private final SongServiceImpl songService;
    private final ArtistServiceImpl artistService;
    private String wantedSId = "";
    private Long wantedAId = null;
    public SongDetailsServlet(SpringTemplateEngine springTemplateEngine, SongServiceImpl songService, ArtistServiceImpl artistService) {
        this.springTemplateEngine = springTemplateEngine;
        this.songService = songService;
        this.artistService = artistService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        IWebExchange exchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);

        WebContext context = new WebContext(exchange);

        songService.addArtistToSong(artistService.findById(wantedAId).orElse(null), songService.findByTrackId(wantedSId));
        //songService.findByTrackId(wantedSId).getPerformers().add(artistService.findById(wantedAId).orElse(null));
        context.setVariable("wantedSong",songService.findByTrackId(wantedSId));
        context.setVariable("wantedArtist",artistService.findById(wantedAId));
        springTemplateEngine.process("songDetails.html",context,resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        wantedSId = req.getParameter("trackId");
        wantedAId = Long.valueOf(req.getParameter("artistId"));
        resp.sendRedirect("/showDetails");
    }
}
