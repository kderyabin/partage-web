package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.BoardPersonTotal;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.utils.StaticResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * BoardController manages display of the board's details.
 */
@Controller
public class BoardDetailsController {
    final private Logger LOG = LoggerFactory.getLogger(BoardDetailsController.class);

    private StorageManager storageManager;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    /**
     * Loads chart data from DB and prepares it for the PieChart
     *
     * @param model BoardModel instance.
     */
    private Map<String, Double> initDataChart(BoardModel model) {
        Map<String, Double> chartData = new HashMap<>();
        LOG.debug("Init chart data");
        List<BoardPersonTotal> list = storageManager.getBoardPersonTotal(model.getId());
        list.forEach(item -> {
            String name = item.getPerson().getName();
            Double amount = item.getTotal().doubleValue();
            chartData.put(name, amount);
        });
        LOG.debug("End Init chart data");
        return chartData;
    }

    /**
     * Displays board's details.
     *
     * @return Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/details")
    public String displayDetails(
            Model viewModel,
            @PathVariable String lang,
            @PathVariable String userId,
            @PathVariable Long boardId,
            HttpServletRequest request
    ) {
        LOG.debug("Displaying details for board: " + boardId);
        Locale locale = new Locale(lang);
        BoardModel model = storageManager.findBoardById(boardId);
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        List<BoardItemModel> items = storageManager.getItems(model);

        HttpSession session = request.getSession(false);
        // If there is a notification in session that's the time to display it.
        Notification notification = (Notification) session.getAttribute("notification");
        session.removeAttribute("notification");
        if (notification == null) {
            notification = new Notification();
        }
        notification.addMessage("chart.expenses", messageSource.getMessage("expenses", null, locale));

        viewModel.addAttribute("notification", notification);

        viewModel.addAttribute("title", model.getName());
        viewModel.addAttribute("currency", model.getCurrencyCode());
        viewModel.addAttribute("model", model);
        viewModel.addAttribute("items", items);
        viewModel.addAttribute("chartData", initDataChart(model));

        // Enable navbar buttons
        viewModel.addAttribute("navbarBtnBackLink", String.format("/%s/app/%s/", lang, userId));
        if (items.size() > 0) {
            viewModel.addAttribute("navbarBtnBalanceLink", "balance");
        }
        viewModel.addAttribute("navbarBtnAddItemLink", "item");
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_CHARTS);
        scripts.add(StaticResources.JS_DETAILS);
        viewModel.addAttribute("scripts", scripts);

        return "app/details.jsp";
    }
}
