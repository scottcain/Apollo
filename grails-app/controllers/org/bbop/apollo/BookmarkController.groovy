package org.bbop.apollo

import grails.converters.JSON
import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

@Transactional(readOnly = true)
class BookmarkController {

//    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def permissionService
    def preferenceService
    def projectionService
    def bookmarkService

    def list() {
        println "loading bookmark . . . "
        JSONObject bookmarkJson = (request.JSON ?: JSON.parse(params.data.toString())) as JSONObject
        User user = permissionService.getCurrentUser(bookmarkJson)
        Organism organism = preferenceService.getCurrentOrganism(user)

        render UserBookmark.findAllByUserAndOrganism(user,organism) as JSON
    }

    def getBookmark(){
        JSONObject bookmarkObject = (request.JSON ?: JSON.parse(params.data.toString())) as JSONObject
        User user = permissionService.currentUser
        Organism organism = preferenceService.getCurrentOrganism(user)

        // creates a projection based on the Bookmarks and caches them
        bookmarkObject.organism = organism.commonName
        projectionService.getProjection(bookmarkObject)

        render bookmarkObject as JSON
    }

    @Transactional
    def addBookmark() {
        JSONArray bookmarkArray = (request.JSON ?: JSON.parse(params.data.toString())) as JSONArray
        User user = permissionService.currentUser
//        Organism organism = preferenceService.getCurrentOrganism(user)
//        println "bookmark array ${bookmarkArray as JSON}"
        Bookmark bookmark = bookmarkService.convertJsonToBookmark(bookmarkArray.getJSONObject(0))
        UserBookmark userBookmark = new UserBookmark(
                bookmark: bookmark
                ,user: user
        ).save()

//        bookmarkArray.getJSONObject(0).getJSONArray("sequenceList")

//        for(int i = 0 ; i < bookmarkArray.size() ; i++){
//            JSONObject bookmarkJson = bookmarkArray.getJSONObject(i)
//            UserBookmark userBookmark = new UserBookmark(
//                    id: bookmarkJson.id
//                    , user: user
//                    , type: bookmarkJson.type
//                    , padding: bookmarkJson.padding
//                    , payload: bookmarkJson.payload
//                    , organism: organism
//                    , sequenceList: (bookmarkJson.sequenceList as JSON).toString()
//            ).save()
////            println "output ${userBookmark as JSON}"
////            println "is valid ${userBookmark.validate()}"
//        }

//        render UserBookmark.findAllByU


//        render UserBookmark.findAllByUserAndOrganism(user,organism) as JSON
        render userBookmark as JSON
    }

    @Transactional
    def deleteBookmark() {
        JSONArray bookmarkJson = (request.JSON ?: JSON.parse(params.data.toString())) as JSONArray
        User user = permissionService.getCurrentUser(new JSONObject())
        Organism organism = preferenceService.getCurrentOrganism(user)
        println "trying to delete bookmarkJSON ${bookmarkJson as JSON}"

        def idList = []
        for(int i = 0 ; i < bookmarkJson.size() ; i++){
            idList.add(bookmarkJson.getJSONObject(i).id)
        }


        UserBookmark.deleteAll(UserBookmark.findAllByIdInList(idList))

        render UserBookmark.findAllByUserAndOrganism(user,organism) as JSON
    }
}