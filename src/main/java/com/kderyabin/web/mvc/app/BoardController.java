package com.kderyabin.web.mvc.app;

import com.kderyabin.core.model.BoardItemModel;
import com.kderyabin.core.model.BoardModel;
import com.kderyabin.core.model.BoardPersonTotal;
import com.kderyabin.core.model.RefundmentModel;
import com.kderyabin.core.services.BoardBalance;
import com.kderyabin.core.services.StorageManager;
import com.kderyabin.web.bean.Notification;
import com.kderyabin.web.services.SettingsService;
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
import java.math.BigDecimal;
import java.util.*;

/**
 * BoardController manages display of everything related to the board.
 */
@Controller
public class BoardController {
    final private Logger LOG = LoggerFactory.getLogger(BoardController.class);

    private StorageManager storageManager;

    private SettingsService settingsService;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Autowired
    public void setStorageManager(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @Autowired
    public void setSettingsService(SettingsService settingsService) {
        this.settingsService = settingsService;
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
        notification.addMessage("chart.expenses", messageSource.getMessage("expenses", null, settingsService.getLanguage()));

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

    /**
     * Displays board's balance page.
     *
     * @param viewModel     Model instance
     * @param boardId       Board ID
     * @return              Template name
     */
    @GetMapping("{lang}/app/{userId}/board/{boardId}/balance")
    public String displayBalance(Model viewModel, @PathVariable Long boardId) {
        LOG.debug("Displaying balance for board: " + boardId);
        BoardModel model = storageManager.findBoardById(boardId);
        if (model == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        BoardBalance boardBalance = BoardBalance.buildFor(storageManager.getBoardPersonTotal(boardId));
        LOG.debug("Balance size: " + boardBalance.getTotals().size());

        viewModel.addAttribute("balances", boardBalance.getTotals());
        // Split debts between participants
        List<RefundmentModel> refundments = getShareData(boardBalance, model.getCurrency());
        viewModel.addAttribute("refundments", refundments);
        // Add chart data
        viewModel.addAttribute("chartData", getShareChart(boardBalance));
        // Set translations for JS
        Notification notification = new Notification();
        Locale language = settingsService.getLanguage();
        notification.addMessage("amount", messageSource.getMessage("amount", null, language));
        viewModel.addAttribute("notification", notification);

        viewModel.addAttribute("title", model.getName());
        viewModel.addAttribute("currency", model.getCurrencyCode());
        viewModel.addAttribute("model", model);
        viewModel.addAttribute("isEmpty", boardBalance.isEmpty());

        // Enable navbar buttons
        viewModel.addAttribute("navbarBtnBackLink", "details");
        viewModel.addAttribute("navbarBtnAddItemLink", "item");
        // Attach JS scripts
        List<String> scripts = new ArrayList<>();
        scripts.add(StaticResources.JS_CHARTS);
        scripts.add(StaticResources.JS_BALANCE);
        viewModel.addAttribute("scripts", scripts);

        return "app/balance.jsp";
    }

    /**
     * Prepares data to display in a refundment table.
     *
     * @param boardBalance Initialized BoardBalance instance
     * @param currency     Currency attached to the board.
     * @return A list of refundments
     */
    private List<RefundmentModel> getShareData(BoardBalance boardBalance, final Currency currency) {
        LOG.debug("Start initShareData");
        List<RefundmentModel> shareData = new ArrayList<>();
        boardBalance.getShare().forEach((debtor, data) -> {
            // Filter if there is an amount to refund
            data.forEach((personModel, decimal) -> {
                if (decimal.compareTo(new BigDecimal("0")) > 0) {

                    RefundmentModel model = new RefundmentModel();
                    model.setAmount(decimal.doubleValue());
                    model.setCreditor(personModel.getName());
                    model.setDebtor(debtor.getName());
                    model.setCurrency(currency);

                    shareData.add(model);
                }
            });
        });
        LOG.debug("End initShareData");
        return shareData;
    }

    /**
     * Prepares data to display in a balance chart which shows paid, overpaid and owed amounts per participant.
     *
     * @param boardBalance Initialized BoardBalance instance
     * @return A map with a participant as a key and its expenses stats with amounts as a value.
     */
    public Map<String, Map<String, Number>> getShareChart(BoardBalance boardBalance) {
        LOG.debug("Start initShareChart");
        Map<String, Map<String, Number>> chartData = new LinkedHashMap<>();
        if (!boardBalance.isEmpty()) {
            // Prepare balances for display in the chart
            BigDecimal average = boardBalance.getAverage();
            // loop through balances
            boardBalance.getBalancePerPerson().forEach((person, balance) -> {
                BigDecimal paid, debt, overpaid;
                if (balance.compareTo(BigDecimal.ZERO) < 0) {
                    // Negative balance: person owes moneys
                    // convert negative balance (money owed to other participants) into spent money
                    paid = average.add(balance);
                    debt = average.subtract(paid);
                    overpaid = new BigDecimal("0");
                } else {
                    paid = average;
                    debt = new BigDecimal("0");
                    overpaid = balance;
                }
                Map<String, Number> personStats = new HashMap<>();
                personStats.put("paid", paid);
                personStats.put("debt", debt);
                personStats.put("overpaid", overpaid);
                chartData.put(person.getName(), personStats);
            });
        }
        LOG.debug("End initShareChart");

        return chartData;
    }
}
