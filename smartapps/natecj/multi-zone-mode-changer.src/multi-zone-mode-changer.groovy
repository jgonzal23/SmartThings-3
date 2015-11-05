/**
 *  Copyright 2015 Nathan Jacobson <natecj@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

definition(
  name: "Multi Zone Mode Changer",
  namespace: "natecj",
  author: "Nathan Jacobson",
  description: "Change modes based on one or more switches being 'on' in multiple zones.",
  category: "Convenience",
  iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
  iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png"
)

preferences {
	section("Zones"){
    input "zone1switches", "capability.switch", title: "Upstairs Switches", multiple: true
    input "zone2switches", "capability.switch", title: "Downstairs Switches", multiple: true
  }
	section("Modes") {
	  input "modeAllOn", "mode", title: "All On", defaultValue: "Home"
	  input "modeAllOff", "mode", title: "All Off", defaultValue: "Away"
	  input "modeOnlyZone1", "mode", title: "Only Upstairs", defaultValue: "Night"
	  input "modeOnlyZone2", "mode", title: "Only Downstairs", defaultValue: "Day"
	}
}

def installed() {
  initialize()
}

def updated() {
  unsubscribe()
  initialize()
}

def initialize() {
	subscribe(zone1switches, "switch", switchHandler)
	subscribe(zone2switches, "switch", switchHandler)
}

def switchHandler(evt) {
  def zone1on = zone1switches.any{ it.currentValue('switch') == 'on' }
  def zone2on = zone2switches.any{ it.currentValue('switch') == 'on' }

  if (zone1on && zone2on) {
    setLocationMode(modeAllOn)
  } else if (!zone1on && !zone2on) {
    setLocationMode(modeAllOff)
  } else if (zone1on && !zone2on) {
    setLocationMode(modeOnlyZone1)
  } else if (!zone1on && zone2on) {
    setLocationMode(modeOnlyZone2)
  }
}
