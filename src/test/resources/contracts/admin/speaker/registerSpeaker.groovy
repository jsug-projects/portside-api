package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url '/speakers'
        headers {
            contentType('application/json')
            header("Authorization","Basic Zm9vOmJhcg==")
        }
        body([
                "name":$(anyNonBlankString()),
                "belongTo":$(anyNonBlankString()),
                "profile":$(anyNonBlankString()),
                "imageUrl":$(anyUrl())
        ])
    }
    response {
        status 201
        headers {
            header 'Location': $(regex('.*'))
        }
    }
}