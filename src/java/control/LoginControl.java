/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dao.DAO;
import entity.Account;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author hung4
 */
@WebServlet(name = "LoginControl", urlPatterns = {"/login"})
public class LoginControl extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //b1 get user,pass frim cookie
        Cookie arr[] = request.getCookies();
        if (arr != null) {//loi dong browser va over setMaxAge thi lost everything => ko dung for o duoi dc
            for (Cookie o : arr) {// tranh loi cookie don't have anything
                if (o.getName().equals("userC")) {
                        request.setAttribute("user", o.getValue());
                }
                if (o.getName().equals("passC")) {
                    request.setAttribute("pass", o.getValue());
                }
            }
        }

        //b2: ser user,pass to login  form
        request.getRequestDispatcher("Login.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("user");
        String password = request.getParameter("pass");
        String remember = request.getParameter("remember");
        DAO dao = new DAO();
        Account a = dao.login(username, password);
        if (a == null) {
            request.setAttribute("mess", "wrong username or pass");
            request.getRequestDispatcher("Login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("acc", a);
            //request.getRequestDispatcher("home").forward(request, response); chuyen trang can data
            //session.setMaxInactiveInterval(1000000); cho phep luu login trong 1 khoang thoi gian

            //luu account len tren côkie
            Cookie u = new Cookie("userC", username);
            Cookie p = new Cookie("passC", password);
            u.setMaxAge(60);
            if (remember != null) {
                p.setMaxAge(60);
            } else {
                p.setMaxAge(0);
            }

            response.addCookie(u);// luu u va p len tren browser
            response.addCookie(p);
            response.sendRedirect("home");//chuyen trang ko can truyen data
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
