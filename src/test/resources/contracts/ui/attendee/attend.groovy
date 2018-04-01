package contracts.ui.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url '/attendees'
        headers {
            contentType('application/json')
        }
        body([
                ids: [
                        "199f7072-e207-486f-94cd-3b259a4305ff",
                        "299f7072-e207-486f-94cd-3b259a4305ff"
                ],
                "email":$(anyEmail()),
        ])
    }
    response {
        status 201
        headers {
            header 'Location': $(regex('.*'))
        }
    }
}

