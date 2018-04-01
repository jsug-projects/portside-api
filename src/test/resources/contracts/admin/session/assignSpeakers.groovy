package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url '/sessions/699f7072-e207-486f-94cd-3b259a4305ff/assignSpeakers'
        headers {
            contentType('application/json')
            header("Authorization","Basic Zm9vOmJhcg==")
        }
        body(
            speakerIds: [
                    "799f7072-e207-486f-94cd-3b259a4305ff",
                    "899f7072-e207-486f-94cd-3b259a4305ff"
            ]
        )
    }
    response {
        status 204
    }
}