package com.example.demo.logging;

import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.users.domain.Users;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
	
	@Around("execution(* com.example.demo..controller..*(..))")
	public Object logController(ProceedingJoinPoint joinPoint) throws Throwable {
		
		long start = System.currentTimeMillis();
		
		// 현재 요청 가져오기
		ServletRequestAttributes attr = 
				(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		
		HttpServletRequest req = (attr != null) ? attr.getRequest() : null;
		
		// http 메소드, 요청 uri
		String httpMethod = (req != null) ? req.getMethod() : "UNKNOWN";
		String uri = (req != null) ? req.getRequestURI() : "UNKNOWN";
		
		// 요청 IP
		String ip = (req != null) ? req.getRemoteAddr() : "UNKNOWN";
		
		// 쿼리 파라미터 정보
		Map<String, String[]> params = (req != null) ? req.getParameterMap() : Map.of();
		
		// request body 정보
		Object[] args = joinPoint.getArgs();
		
		// 호출된 컨트롤러와 메서드 정보
		String className = joinPoint.getSignature().getDeclaringTypeName();
		String methodName = joinPoint.getSignature().getName();
		
		// JWT 사용자 정보 출력
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userInfo = "anonymous";
		if (authentication != null & authentication.getPrincipal() instanceof Users user) {
			Users user = (Users) authentication.getPrincipal();
			userInfo = "userId=" + user.getId() + ", nickname=" + user.getNickname();
		}
		
        log.info("======================================================");
        log.info("[REQUEST] {} {}", httpMethod, uri);
        log.info("[IP] {}", ip);
        log.info("[USER_INFO] {}", userInfo);
        log.info("[HANDLER] {}.{}()", className, methodName);

        // query parameter 출력
        if (!params.isEmpty()) {
            log.info("[QUERY PARAMS] {}", formatParams(params));
        }

        // request body 출력
        if (args != null && args.length > 0) {
            log.info("[BODY/ARGS] {}", Arrays.toString(args));
        }
		
		try {
			Object result = joinPoint.proceed();
			
			long end = System.currentTimeMillis();
			
            // 응답 status code 출력
            if (result instanceof ResponseEntity<?> responseEntity) {
                log.info("[RESPONSE STATUS] {}", responseEntity.getStatusCode());
            }
            
			log.info("[SUCCESS] {} {} ({}ms)", httpMethod, uri, (end - start));
			log.info("======================================================");
			
			return result;
		} catch (Exception e) {
            long end = System.currentTimeMillis();

            log.error("[ERROR] {} {} ({}ms)", httpMethod, uri, (end - start));
            log.error("[ERROR MESSAGE] {}", e.getMessage());
            log.info("======================================================");

            throw e;
		}
	}
	
	private String formatParams(Map<String, String[]> params) {
		StringBuilder sb = new StringBuilder();
		
		params.forEach((k, v) -> {
			sb.append(k).append("=").append(Arrays.toString(v)).append(" ");
		});
		return sb.toString().trim();
	}
}
