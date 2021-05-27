package br.com.zup.ot4.carro

import br.com.zup.ot4.CarroRequest
import br.com.zup.ot4.CarroResponse
import br.com.zup.ot4.CarrosGrpcServiceGrpc
import io.grpc.Status
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.validation.ConstraintViolationException

@Singleton
class CarrosEndpoint(
    val carrosRepository: CarrosRepository
) : CarrosGrpcServiceGrpc.CarrosGrpcServiceImplBase() {

    override fun adicionar(
        request: CarroRequest,
        responseObserver: StreamObserver<CarroResponse>
    ) {
        if(carrosRepository.existsByPlaca(request.placa)){
            responseObserver.onError(Status.ALREADY_EXISTS
                .withDescription("Carro com placa existente")
                .asRuntimeException())
            return
        }

        val carro = Carro(request.modelo, request.placa)

        try {
            carrosRepository.save(carro)
        } catch (e: ConstraintViolationException){
            responseObserver.onError(Status.INVALID_ARGUMENT
                .withDescription("dados de entrada inválidos")
                .asRuntimeException())
            return
        }

        responseObserver.onNext(carro.id?.let { CarroResponse.newBuilder().setId(it).build() })
        responseObserver.onCompleted()
    }
}