package controller;

import bean.Tour;
import bean.Order;
import bean.User;
import bean.enums.OrderStatus;
import bean.enums.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import service.ClientService;
import service.TouragencyService;
import service.exception.ServiceException;
import service.factory.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Controller extends HttpServlet {
    private final ClientService clientService = ServiceFactory.getInstance().getClientService();
    private final TouragencyService touragencyService = ServiceFactory.getInstance().getTouragencyService();
    private final Logger logger = (Logger) LogManager.getLogger();
    private ResourceBundle bundle = ResourceBundle.getBundle("text");

    private String dispatch(String url, String destination) {
        return url.substring(0, url.lastIndexOf("/")) + "/" + destination;
    }

    private void signIn(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        try {
            user = clientService.signIn(user);
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
            } else {
                request.setAttribute("errorMessage", bundle.getString("incorrect_login_password"));
                request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("empty_fields"));
            request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
        }
    }

    private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        var user = new User();
        user.setLogin(request.getParameter("login"));
        user.setPassword(request.getParameter("password"));
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setRole(UserRole.USER);
        try {
            user = clientService.signUp(user);
            if (user != null) {
                session.setAttribute("user", user);
                response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
            } else {
                request.setAttribute("errorMessage", bundle.getString("login_is_busy"));
                request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
            }
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("empty_fields"));
            request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
        }
    }

    private void signOut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        response.sendRedirect(request.getContextPath() + "/touragency/sign-in");
    }

    private void serveUserCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String searchRequest = request.getParameter("search");
        if (searchRequest == null) {
            try {
                request.setAttribute("tours", touragencyService.gettourList());
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_tour_list"));
            }
        } else {
            try {
                List<tour> tourList = touragencyService.searchtours(searchRequest);
                request.setAttribute("tours", tourList);
                if (tourList.size() == 0) {
                    request.setAttribute("noElements", bundle.getString("nothing_found"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_searching"));
            }
        }
        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
    }

    private void serveLibrarianCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String tourId = request.getParameter("tourId");
        if (action != null) {
            switch (action) {
                case "delete" -> {
                    try {
                        if (tourId != null) {
                            tour tour = touragencyService.gettourById(tourId);
                            touragencyService.deletetour(tour);
                        }
                        response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                    } catch (ServiceException e) {
                        logger.error(e.getMessage());
                        request.setAttribute("errorMessage", bundle.getString("error_deleting"));
                        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
                    }
                }
                case "edit" -> {
                    try {
                        request.setAttribute("tour", touragencyService.gettourById(tourId));
                        request.setAttribute("type", "edit");
                        request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                    } catch (ServiceException e) {
                        logger.error(e.getMessage());
                        request.setAttribute("errorMessage", bundle.getString("error_getting"));
                        request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
                    }
                }
                case "add" -> {
                    request.setAttribute("type", "add");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                }
            }
        } else {
            try {
                request.setAttribute("tours", touragencyService.gettourList());
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_tour_list"));
            }
            request.getRequestDispatcher("/WEB-INF/jsp/catalog.jsp").forward(request, response);
        }
    }

    private void serveCatalogPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        if (user.getRole() == UserRole.USER) {
            serveUserCatalogPage(request, response);
        } else {
            serveLibrarianCatalogPage(request, response);
        }
    }

    private List<Order> getUserOrders(String tourId, User user, String action, boolean langIsSet) throws ServiceException {
        if (tourId != null && !langIsSet) {
            tour tour = touragencyService.gettourById(tourId);
            var order = new Order();
            order.setUser(user);
            order.settour(tour);
            if (action == null) {
                order.setStatus(OrderStatus.PROCESSING);
                touragencyService.addNewOrder(order);
            } else {
                touragencyService.deleteOrder(order);
            }
        }
        return touragencyService.getUserOrders(user);
    }

    private List<Order> getOrders(String tourId, String userLogin, String action, boolean langIsSet)
            throws ServiceException {
        if (tourId != null && !langIsSet) {
            User user = clientService.getUserByLogin(userLogin);
            tour tour = touragencyService.gettourById(tourId);
            var order = new Order();
            order.setUser(user);
            order.settour(tour);
            switch (action) {
                case "reading_room" -> {
                    order.setStatus(OrderStatus.READING_ROOM);
                    touragencyService.updateOrder(order);
                }
                case "subscription" -> {
                    order.setStatus(OrderStatus.SUBSCRIPTION);
                    touragencyService.updateOrder(order);
                }
                case "cancel" -> {
                    touragencyService.deleteOrder(order);
                }
            }
        }
        return touragencyService.getOrders();
    }

    private void serveOrdersPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            User user = (User) request.getSession().getAttribute("user");
            String action = request.getParameter("action");
            String userLogin = request.getParameter("userLogin");
            String tourId = request.getParameter("tourId");
            boolean langIsSet = request.getParameter("lang") != null;
            List<Order> orders;
            if (user.getRole() == UserRole.USER) {
                orders = getUserOrders(tourId, user, action, langIsSet);
            } else {
                orders = getOrders(tourId, userLogin, action, langIsSet);
            }
            if (orders.stream().noneMatch(order -> order.getStatus() == OrderStatus.PROCESSING)) {
                request.setAttribute("noProcessing", bundle.getString("no_processing"));
            }
            if (orders.stream().noneMatch(order -> order.getStatus() != OrderStatus.PROCESSING)) {
                request.setAttribute("noActive", bundle.getString("no_active"));
            }
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/jsp/orders.jsp").forward(request, response);
        } catch (ServiceException e) {
            logger.error(e.getMessage());
            request.setAttribute("errorMessage", bundle.getString("error_order_list"));
        }
    }

    private void addtour(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String count = request.getParameter("count");
        if (title != null && description != null && count != null) {
            try {
                var tour = new tour();
                tour.setTitle(title);
                tour.setDescription(description);
                tour.setCount(Integer.parseInt(count));
                if (!touragencyService.addNewtour(tour)) {
                    request.setAttribute("errorMessage", bundle.getString("error_add_tour_data"));
                    request.setAttribute("type", "add");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_add_tour"));
                request.setAttribute("type", "add");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_count"));
                request.setAttribute("type", "add");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            }
        }
    }

    private void edittour(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String count = request.getParameter("count");
        if (id != null && title != null && description != null && count != null) {
            try {
                var tour = new tour();
                tour.setId(Integer.parseInt(id));
                tour.setTitle(title);
                tour.setDescription(description);
                tour.setCount(Integer.parseInt(count));
                if (!touragencyService.addEditedtour(tour)) {
                    request.setAttribute("errorMessage", bundle.getString("error_edit_tour_data"));
                    request.setAttribute("tour", tour);
                    request.setAttribute("type", "edit");
                    request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
                } else {
                    response.sendRedirect(dispatch(request.getRequestURL().toString(), "catalog"));
                }
            } catch (ServiceException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_edit_tour"));
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                logger.error(e.getMessage());
                request.setAttribute("errorMessage", bundle.getString("error_count"));
                request.setAttribute("type", "edit");
                request.getRequestDispatcher("/WEB-INF/jsp/add-edit.jsp").forward(request, response);
            }
        }
    }

    private void setLangIfPresent(HttpServletRequest request) {
        String lang = request.getParameter("lang");
        if (lang != null) {
            request.getSession().setAttribute("lang", lang);
            bundle = ResourceBundle.getBundle("text", new Locale(lang));
            String query = request.getQueryString();
            request.setAttribute("queryWithLang", query.substring(0, query.lastIndexOf("=")) + "=");
        }
    }

    private String getActionFromURI(String URI) {
        String[] arr = URI.split("/");
        return arr[arr.length - 1];
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        setLangIfPresent(request);
        String action = getActionFromURI(request.getRequestURI());
        switch (action) {
            case "sign-in" -> request.getRequestDispatcher("/WEB-INF/jsp/sign-in.jsp").forward(request, response);
            case "sign-up" -> request.getRequestDispatcher("/WEB-INF/jsp/sign-up.jsp").forward(request, response);
            case "sign-out" -> signOut(request, response);
            case "catalog" -> serveCatalogPage(request, response);
            case "orders" -> serveOrdersPage(request, response);
            case "addtour" -> addtour(request, response);
            case "edittour" -> edittour(request, response);
            default -> request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(request, response);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = getActionFromURI(request.getRequestURI());
        switch (action) {
            case "sign-in" -> signIn(request, response);
            case "sign-up" -> signUp(request, response);
            default -> request.getRequestDispatcher("/WEB-INF/jsp/404.jsp").forward(request, response);
        }
    }
}