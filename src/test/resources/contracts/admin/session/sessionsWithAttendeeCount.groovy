package contracts.admin.session

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/sessions/withAttendeeCount'
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                [
					"session":[
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
					"attendeeCount":$(anyNonBlankString())
                ],
        ])
        testMatchers {
            jsonPath('$', byType {
                minOccurrence(10)
                maxOccurrence(10)
            })
            jsonPath('$[0].session.speakers', byType {
                minOccurrence(7)
                maxOccurrence(7)
            })
        }
    }
}