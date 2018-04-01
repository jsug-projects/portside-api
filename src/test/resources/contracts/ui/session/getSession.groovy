package contracts.ui.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/sessions/699f7072-e207-486f-94cd-3b259a4305ff'
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body(
            "id": $(anyUuid()),
            "title": $(anyNonBlankString()),
            "description":$(anyNonBlankString()),
            "speaker":$(anyNonBlankString()),
            "speakers": [
                    [
                            "id":$(anyUuid()),
                            "name":$(anyNonBlankString()),
                            "belongTo":$(anyNonBlankString()),
                            "profile":$(anyNonBlankString()),
                            "imageUrl":$(null),//null でも 文字列でも良いという指定がしていな
                    ]
            ]

        )
    }
}

