package me.examplecloud.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Order(-1)
@Component
class CustomGlobalFilter : GlobalFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val path = request.uri.path
        val method = request.method!!.name
        val ipAddr = request.remoteAddress?.address?.hostAddress

        println("GlobalFilter Start: [$method][$path]")

        // header setting
        exchange.request
            .mutate()
            .headers{ h -> h.set("X-Forwarded-For", ipAddr)}

        return chain.filter(exchange).then(Mono.fromRunnable {
            println("GlobalFilter End: [${exchange.response}]")
        })
    }
}