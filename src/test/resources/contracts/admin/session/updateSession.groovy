package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'PUT'
        url '/sessions/699f7072-e207-486f-94cd-3b259a4305ff'
        headers {
            contentType('application/json')
            header("Authorization","Basic Zm9vOmJhcg==")
        }
        body([
                "title": $(anyNonBlankString()),
                "description":$(anyNonBlankString()),
                "speaker":$(anyNonBlankString()),
                "speakers": [
                        [
                                "name":$(anyNonBlankString()),
                                "belongTo":$(anyNonBlankString()),
                                "profile":$(anyNonBlankString()),
                                "imageUrl":$(null),//null でも 文字列でも良いという指定がしていな
                        ],
                        [
                                "name":$(anyNonBlankString()),
                                "belongTo":$(anyNonBlankString()),
                                "profile":$(anyNonBlankString()),
                                "imageUrl":$(null),//null でも 文字列でも良いという指定がしていな
                        ]
                ]
        ])
    }
    response {
        status 204
    }
}