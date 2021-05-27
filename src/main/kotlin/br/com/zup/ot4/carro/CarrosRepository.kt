package br.com.zup.ot4.carro

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface CarrosRepository: JpaRepository<Carro, Long> {
    fun existsByPlaca(placa: String): Boolean
}