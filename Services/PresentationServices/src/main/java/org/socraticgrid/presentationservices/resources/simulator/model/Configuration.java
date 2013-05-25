/*
 * ****************************************************************************************************************
 *  *
 *  * Copyright (C) 2012 by Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
 *  * with the License. You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License is
 *  * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and limitations under the License.
 *  *
 *  ****************************************************************************************************************
 *
 * ****************************************************************************************************************
 *  * Socratic Grid contains components to which third party terms apply. To comply with these terms, the following
 *  * notice is provided:
 *  *
 *  * TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION
 *  * Copyright (c) 2008, Nationwide Health Information Network (NHIN) Connect. All rights reserved.
 *  * Redistribution and use in source and binary forms, with or without modification, are permitted provided that
 *  * the following conditions are met:
 *  *
 *  * - Redistributions of source code must retain the above copyright notice, this list of conditions and the
 *  *     following disclaimer.
 *  * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the
 *  *     following disclaimer in the documentation and/or other materials provided with the distribution.
 *  * - Neither the name of the NHIN Connect Project nor the names of its contributors may be used to endorse or
 *  *     promote products derived from this software without specific prior written permission.
 *  *
 *  * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 *  * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION HOWEVER
 *  * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *  *
 *  * END OF TERMS AND CONDITIONS
 *  *
 *  ****************************************************************************************************************
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.socraticgrid.presentationservices.resources.simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.restlet.data.Form;
import org.restlet.data.Parameter;

/**
 *
 * @author tnguyen
 */
public class Configuration {

    public String configId;
    public String name;
    public String description;
    public String createdDate;
    public String lastRunDate;
    public String author;
//    public List<String> goals;
    public String goal;
    public String startDate;
    public String endDate;
    public List<String> durations;
    public String duration;
    public String stopIteration;
    public String stopTime;
    public String timeUnit;
    public String stopScore;
    public String stopImprovement;
    public List<Agent> agents;
    public List<Constraint> constraints;

    public List<Agent> getAgents() {
        return this.agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Constraint> getConstraints() {
        return this.constraints;
    }

    public void setConstraints(List<Constraint> constraints) {
        this.constraints = constraints;
    }



    private Pattern p = Pattern.compile("(.+)\\[(.+)\\]\\[(.+)\\]");
    private Matcher m;

    /**
     * Parse the incoming params for each AGENT array item and create an
     * array list of all agents ... to be sent to DSA.
     * @param form
     * @return
     */
    public List<Agent> getAllAgents(Form form) {

        List<Agent> agents = new ArrayList();


        for (Parameter parameter : form) {
            System.out.print("\n===> parameter= " + parameter.getName());
            System.out.println("  " + parameter.getValue());

            //Find all agents and push into List<Agents>
            if (parameter.getName().startsWith("agents")) {

                String val = parameter.getName();

                m = p.matcher(val);
                while(m.find()) {
                   System.out.println("Size = "+ m.group().length());
                   System.out.println("0: "+ m.group(0));
                   System.out.println("1: "+ m.group(1)); //"agents"
                   System.out.println("2: "+ m.group(2)); // 0|1|2
                   System.out.println("3: "+ m.group(3)); // "population"

                   Agent a = new Agent();
                   if (m.group(3).equalsIgnoreCase("filtered")) {
                       a.filtered = parameter.getValue();
                   }
//                   if (m.group(3).equalsIgnoreCase("table")) {
//                       a.table = parameter.getValue();
//                   }
                   if (m.group(3).equalsIgnoreCase("population")) {
                       a.filtered = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("subfilter")) {
                       a.subfilter = parameter.getValue();
                   }
                   agents.add(a);
                }
            } //end-if-agents
        }
        return agents;
    }

    /**
     * Parse the incoming params for each CONSTRAINT array item and create an
     * array list of all constraints ... to be sent to DSA.
     *
     * @param form
     * @return
     */
    public List<Constraint> getAllConstraints(Form form) {

        List<Constraint> constraints = new ArrayList();


        for (Parameter parameter : form) {
            System.out.print("\n===> parameter= " + parameter.getName());
            System.out.println("  " + parameter.getValue());

            //Find all agents and push into List<Agents>
            if (parameter.getName().startsWith("constraints")) {

                String val = parameter.getName();

                m = p.matcher(val);
                while(m.find()) {
                   System.out.println("Size = "+ m.group().length());
                   System.out.println("0: "+ m.group(0));
                   System.out.println("1: "+ m.group(1)); //"constraints"
                   System.out.println("2: "+ m.group(2)); // 0|1|2
                   System.out.println("3: "+ m.group(3)); // "importance"

                   Constraint a = new Constraint();
                   if (m.group(3).equalsIgnoreCase("importance")) {
                       a.importance = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("required")) {
                       a.required = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("included")) {
                       a.included = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("packageId")) {
                       a.packageId = parameter.getValue();
                   }
                   constraints.add(a);
                }
            } //end-if-agents
        }
        return constraints;
    }
}
/*
class Agent {
//    public String agentId; //out
//    public String name;    //out
//    public String type;    //out
    public String table;
    public String population;
//    public List<String> subfilters; //out
//    public String subFilter; //out
    public String filtered;
    public String populationRange;
}

class Constraint {
    public String name;
    public String type;
    public String importance;
    public String required;
    public String included;
}
 * 
 */