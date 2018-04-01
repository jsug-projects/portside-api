package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url '/speakers/799f7072-e207-486f-94cd-3b259a4305ff/image'
        headers {
            contentType('multipart/form-data;boundary=XXX')
            header("Authorization","Basic Zm9vOmJhcg==")
        }
        multipart(
            data: named(
                name: value(anyNonEmptyString()),
                content: value(anyNonEmptyString())
            )
        )
    }
    response {
        status 201
    }
}