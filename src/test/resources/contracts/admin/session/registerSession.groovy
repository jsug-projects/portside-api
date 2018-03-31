package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'POST'
        url '/sessions'
        headers {
            contentType('application/json')
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
        status 201
        headers {
            header 'Location': $(regex('.*'))
        }
    }
}