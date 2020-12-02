//
//  GetCommandIntentHandler.swift
//  CommandIntent
//
//  Created by Amin Fathi on 07/11/2020.
//

import Foundation
import Messages

class GetCommandInentHandler: NSObject, GetCommandIntentHandling{

  
  func handle(intent: GetCommandIntent, completion: @escaping (GetCommandIntentResponse) -> Void) {
    let req = intent.request!
    let res = "the result is " + req
    
    let response = GetCommandIntentResponse.success(result: res)
        completion(response)
  }
  
//  func resolveRequest(for intent: GetCommandIntent, with completion: @escaping (GetCommandRequestResolutionResult) -> Void) {
//      var result: GetCommandRequestResolutionResult = .unsupported()
//
//      defer { completion(result) }
//
//      if let req = intent.request {
//        result = GetCommandRequestResolutionResult.success(with: req)
//      }
//    }
}


