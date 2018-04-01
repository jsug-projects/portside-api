package contracts.ui.session

import org.springframework.test.context.TestPropertySource

org.springframework.cloud.contract.spec.Contract.make {
    request {
        method 'GET'
        url '/attendees'
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
                    "id": $(anyUuid()),
                    "email": $(anyNonBlankString()),
                ],
        ])
        testMatchers {
            jsonPath('$', byType {
                minOccurrence(1)
            })
        }
    }
}

