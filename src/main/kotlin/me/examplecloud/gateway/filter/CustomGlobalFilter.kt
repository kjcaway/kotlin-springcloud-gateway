package me.examplecloud.gateway.filter

import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CustomGlobalFilter : GlobalFilter {
    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        val request = exchange.request
        val path = request.uri.path
        val method = request.method!!.name

        println("GlobalFilter Start: [$method][$path]")

        return chain.filter(exchange).then(Mono.fromRunnable {
            println("GlobalFilter End: [${exchange.response}]")
        })
    }
}