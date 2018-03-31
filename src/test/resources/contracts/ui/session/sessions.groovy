package contracts.ui.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/sessions'
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                [
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
                ],
        ])
        testMatchers {
            jsonPath('$', byType {
                minOccurrence(10)
                maxOccurrence(10)
            })
            jsonPath('$[0].speakers', byType {
                minOccurrence(7)
                maxOccurrence(7)
            })
        }
    }
}

