package net.catten.ssim.web.interceptors;

import net.catten.ssim.web.services.SimpleUserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by cattenlinger on 2017/3/9.
 */
public class PermissionInterceptor implements HandlerInterceptor {

    private SimpleUserServices userServices;

    @Autowired
    public void setUserServices(SimpleUserServices userServices) {
        this.userServices = userServices;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("SSIM-AUTH-TOKEN");
        if(token == null || token.equals("") || userServices.isTokenExpired(token)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }else return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
