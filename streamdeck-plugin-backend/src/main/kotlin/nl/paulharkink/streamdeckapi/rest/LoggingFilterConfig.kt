package nl.paulharkink.streamdeckapi.rest

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import nl.paulharkink.streamdeckapi.info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.web.util.ContentCachingRequestWrapper

@Configuration
@Profile("rest")
class LoggingFilterConfig {

    @Bean
    fun loggingFilter(): Filter {
        return Filter { request: ServletRequest, response: ServletResponse, chain: FilterChain ->
            val wrappedRequest: ServletRequest = (request as? HttpServletRequest)?.let { httpServletRequest ->
                info("Incoming request data: method: ${httpServletRequest.method}, url: ${httpServletRequest.requestURL}")
                ContentCachingRequestWrapper(httpServletRequest)
            } ?: run {
                info("Incoming request: $request")
                request
            }

            try {
                chain.doFilter(wrappedRequest, response)
            } finally {
                (wrappedRequest as? ContentCachingRequestWrapper)
                    ?.let { info("Request body: ${wrappedRequest.contentAsByteArray.toString(Charsets.UTF_8)}") }
            }
            (response as? HttpServletResponse)?.let { httpServletResponse ->
                info("Outgoing response status: ${httpServletResponse.status}")
            }
        }
    }
}