package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url '/speakers/799f7072-e207-486f-94cd-3b259a4305ff'
        headers {
            contentType('application/json')
            header("Authorization","Basic Zm9vOmJhcg==")
        }
        body([
                "name":"",
                "belongTo":$(anyNonBlankString()),
                "profile":$(anyNonBlankString()),
                "imageUrl":$(anyUrl())
        ])
    }
    response {
        status 400
    }
}