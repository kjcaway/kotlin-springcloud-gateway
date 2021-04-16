package me.examplecloud.gateway.filter

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.cors.reactive.CorsUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class CustomWebFilter: WebFilter {
    private final val ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type, Authorization, credential, X-XSRF-TOKEN"
    private final val ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS"
    private final val ALLOWED_ORIGIN = "*"
    private final val MAX_AGE = "3600"

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request

        if(CorsUtils.isCorsRequest(request)){
            val response = exchange.response
            response.headers.let{
                it.add("Access-Control-Allow-Headers", ALLOWED_HEADERS)
                it.add("Access-Control-Allow-Methods", ALLOWED_METHODS)
                it.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN)
                it.add("Access-Control-Max-Age", MAX_AGE)
            }

            if(request.method == HttpMethod.OPTIONS){
                response.statusCode = HttpStatus.OK
                return Mono.empty()
            }
        }
        return chain.filter(exchange)
    }
}