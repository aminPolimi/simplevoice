//
//  IntentHandler.swift
//  CommandIntent
//
//  Created by Amin Fathi on 07/11/2020.
//

import Intents

class IntentHandler: INExtension {
    
    override func handler(for intent: INIntent) -> Any {
      let getCommend = GetCommandInentHandler()
      return getCommend
    }
}
