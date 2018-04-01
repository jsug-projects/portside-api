package contracts.admin.speaker

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/speakers'
        headers {
            header("Authorization","Basic Zm9vOmJhcg==")
        }
    }
    response {
        status 200
        headers {
            contentType('application/json')
        }
        body([
                [
                    "name": $(anyNonBlankString()),
                    "belongTo": $(anyNonBlankString()),
                    "profile": $(anyNonBlankString()),
                    "imageUrl": $(null)
                ]
        ])
        testMatchers {
            jsonPath('$', byType {
                minOccurrence(1)
            })
        }
    }
}