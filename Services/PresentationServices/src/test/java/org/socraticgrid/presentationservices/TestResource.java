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
package org.socraticgrid.presentationservices;


import java.util.Set;
import org.restlet.data.Form;
import org.socraticgrid.presentationservices.helpers.SynchronousRequestHelperFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;
import org.socraticgrid.presentationservices.utils.factQuery.EcsQuery;
import org.junit.After;
import org.junit.Before;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import org.socraticgrid.util.SessionUtilities;
import java.io.IOException;
import java.util.Iterator;

import org.socraticgrid.presentationsservices.facttypes.ecsDetail.PatientDataFact;
import org.socraticgrid.presentationsservices.facttypes.ecsDetail.*;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collections;
import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.restlet.data.MediaType;
import static org.junit.Assert.*;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.resource.Representation;
import org.restlet.util.Series;
import org.socraticgrid.presentationservices.helpers.PropertyHelper;

/**
 *
 * @author nhin
 */
public class TestResource {

    public TestResource() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    


    @Test
    public void test_checkPropertiesRefresh() {

        PropertyHelper ph = PropertyHelper.getPropertyHelper();
        String prop = ph.getProperty("useStubbedEcsData");

    }
    
    //@Test
    public String testPOSTWithArray() throws IOException {
        String ret = null;
        String request = "saveConfiguration?"
                +"name=configinfo name 1&description=configinfo desc 1:  Phasellus vestibulum ipsum augue, id molestie purus. Donec viverra orci ac sem tincidunt facilisis. Morbi eget rhoncus nulla. Aliquam porttitor odio vel metus viverra lacinia sit amet quis erat. Nulla fringilla malesuada ante eu dignissim&startDate=2011-11-07 10:00&duration=20&agents[agentId]=&agents[populationRange]=&agents[agentId][1]=1&agents[population][1]=50&agents[filtered][1]=on&agents[subfilter][1]=filter 1&agents[populationRange][1]=40&agents[agentId][2]=2&agents[population][2]=5&agents[subfilter][2]=filter 2&agents[populationRange][2]=80&agents[agentId][3]=3&agents[population][3]=1000&agents[filtered][3]=on&agents[subfilter][3]=filter 3&agents[populationRange][3]=10&constraints[constraintId]=&constraints[constraintId][1]=1&constraints[required][1]=on&constraints[importance][1]=Low&constraints[constraintId][2]=2&constraints[required][2]=on&constraints[importance][2]=Medium&constraints[constraintId][3]=3&constraints[importance][3]=High&constraints[constraintId][4]=4&constraints[required][4]=on&constraints[importance][4]=Medium&constraints[constraintId][5]=5&constraints[importance][5]=Low&constraints[constraintId][6]=6&constraints[required][6]=on&constraints[importance][6]=High&stopTime=3&stopScore=40&stopIteration=1,000&stopImprovement=5";
        
        return ret;
        
    }


    /*

     */
    @Test
    public void testInsertPSStatusStatic() throws IOException {
        String ret = null;
        String successStatus = ",\"successStatus\": true,";
        String statusMessage = "\"statusMessage\": \"This is a message to the user on error status\"";

        String sampleDSA =
"{"
+ "\"riskModelsFact\": {"
//+ "\"successStatus\": true,"
//+ "\"statusMessage\": \"This is a message to the user on error status\","
+ "\"models\": ["
+ "{"
+ "\"modelId\": \"modelId1\","
+ "\"title\": \"PTSD\","
+ "\"disease\": \"PTSD description\","
+ "\"modelType\": \"E\","
+ "\"displayThreshold\": 80"
+ "},"
+ "{"
+ "\"modelId\": \"modelId6\","
+ "\"title\": \"Schizophrenia\","
+ "\"disease\": \"Schizophrenia description\""
+ "}"
+ "]"
+ "}"
+ "}";

        System.out.println(sampleDSA);

        StringBuilder sb = new StringBuilder(sampleDSA);


        int position = getFirstMatch("riskModelsFact", sampleDSA);

        System.out.println( "===> ("+ position+ ")\n" + sampleDSA.substring(position));

        System.out.println("2nd to last position("+ (sampleDSA.length() -2) + "= " + sampleDSA.substring(sampleDSA.length() -2) );
        System.out.println("last position("+ (sampleDSA.length() -1) + "= " + sampleDSA.substring(sampleDSA.length() -1) );

        int posit = sampleDSA.length() -2;

        String BEG = sampleDSA.substring(0,posit);
        String END = sampleDSA.substring(posit);
        String NEW = BEG + successStatus + statusMessage + END;

        
//        System.out.println("BEG= "+BEG);
//        System.out.println("END= "+END);

        System.out.println("NEW= \n"+NEW);

        String errResp =
            "{\n"
            + "    \"riskModelsFact\" : {\n"
            + "        \"successStatus\":false,\n"
            + "        \"statusMessage\":\"" + "ERRROR" + "\",\n";
         System.out.println("ERR= \n"+errResp);

        assert(true);


    }

    @Test
    public void testInsertPSStatus() {
        String jsonRsp =
                "{"
                + "\"riskModelsFact\": {"
                + "\"models\": ["
                + "{"
                + "\"modelId\": \"modelId1\","
                + "\"title\": \"PTSD\","
                + "\"disease\": \"PTSD description\","
                + "\"modelType\": \"E\","
                + "\"displayThreshold\": 80"
                + "},"
                + "{"
                + "\"modelId\": \"modelId6\","
                + "\"title\": \"Schizophrenia\","
                + "\"disease\": \"Schizophrenia description\""
                + "}"
                + "]"
                + "}"
                + "}";
        
        String ret = SynchronousRequestHelperFactory.insertPSStatus(jsonRsp, true, "");
        System.out.println("OK= \n"+ret);

        ret = SynchronousRequestHelperFactory.insertPSStatus(jsonRsp, false, "Had a problem");
        System.out.println("ERR= \n"+ret);

        //------------ EXTRA

        ret = SynchronousRequestHelperFactory.insertPSStatus(jsonRsp, true, "PASSED");
        System.out.println("OK= \n"+ret);



    }

    @Test
    public void testPrepPSStatus() {
        String jsonRsp =
                "{"
                + "\"riskModelsFact\": {"
                + "\"models\": ["
                + "{"
                + "\"modelId\": \"modelId1\","
                + "\"title\": \"PTSD\","
                + "\"disease\": \"PTSD description\","
                + "\"modelType\": \"E\","
                + "\"displayThreshold\": 80"
                + "},"
                + "{"
                + "\"modelId\": \"modelId6\","
                + "\"title\": \"Schizophrenia\","
                + "\"disease\": \"Schizophrenia description\""
                + "}"
                + "]"
                + "}"
                + "}";

        String ret = SynchronousRequestHelperFactory.prepPSStatus(true, "");
        System.out.println("OK= \n"+ret);

        ret = SynchronousRequestHelperFactory.prepPSStatus(false, "Had a problem");
        System.out.println("ERR= \n"+ret);

        //------------ EXTRA
        ret = SynchronousRequestHelperFactory.prepPSStatus(true, "PASSED");
        System.out.println("OK= \n"+ret);


        String root = "{\n\"startDiagnosticGuideProcess\": {";
        String jsonRoot = root + SynchronousRequestHelperFactory.prepPSStatus(true, "");
        String finalJson = jsonRoot + ","+ jsonRsp.substring(1) + "}";
        System.out.println("FINAL=\n"+ finalJson);

        System.out.println( "\n"+
              root
            + SynchronousRequestHelperFactory.prepPSStatus(false, "BAD")
            + "    }\n"
            + "}"
            );

        System.out.println("\n"+SynchronousRequestHelperFactory.prepErrorResponse(root, "FAILURE"));
    }

    private int getFirstMatch(String searchPattern,String textString) {
       int index = -1;
       Pattern pattern = Pattern.compile(searchPattern);
       Matcher matcher = pattern.matcher(textString);

       if(matcher.find()) {
           index = matcher.start();
       }

       return index;
   }


    @Test
    public void testForm() {
        System.out.println("testForm");

        String query =
            "configId=000000000001&modelId=modid1&name=Name of the configuration"
            + "&startDate=2011-11-07 10:00&duration=20"
            + "&agents[0][id]=0001&agents[0][name]=0001&agents[0][filter]=on&agents[0][population]=5"
            + "&agents[1][id]=0001&agents[1][name]=0002&agents[1][population]=50"
            + "&agents[2][id]=0001&agents[2][name]=0003&agents[2][filter]=on&agents[2][population]=5000"
            + "&constraints[0][id]=0001&constraints[0][required]=on&constraints[0][importance]=Low"
            + "&constraints[1][id]=0002&constraints[1][importance]=Medium"
            + "&constraints[2][id]=0003&constraints[2][required]=on&constraints[2][importance]=High"
            ;

        Form form = new Form(query);
//        for (Parameter parameter : form) {
//            System.out.print("===> parameter= " + parameter.getName());
//            System.out.println(" / " + parameter.getValue());
//        }

        List<Agent> agents = this.getAllAgents(form);

        List<Constraint> constraints = this.getAllConstraints(form);

    }


    private List<Agent> getAllAgents(Form form) {
        Pattern p = Pattern.compile("(.+)\\[(.+)\\]\\[(.+)\\]");
        Matcher m;
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
                   if (m.group(3).equalsIgnoreCase("table")) {
                       a.table = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("population")) {
                       a.filtered = parameter.getValue();
                   }
                   agents.add(a);
                }
            } //end-if-agents
        }
        return agents;
    }

    private List<Constraint> getAllConstraints(Form form) {
        Pattern p = Pattern.compile("(.+)\\[(.+)\\]\\[(.+)\\]");
        Matcher m;
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
                   constraints.add(a);
                }
            } //end-if-agents
        }
        return constraints;
    }

    /*
    //=============================================================
    @Test
    public void testForm1() {
        System.out.println("testForm");

        // used fo rtesting getAllAgents1 and getAllConstraints1
        String query =
          "userid=1&modelId=1&configId=111"
        + "&name=configinfo name 1"
        + "&description=configinfo desc 1:  Phasellus vestibulum"
        + "&startDate=2011-11-07 10:00"
        + "&duration=20"

        + "&agents=agentId=1&population=50&filtered=on&subfilter=filter 1,populationRange=40"
        + "&agents=agentId=2,population=5,filtered=off,subfilter=filter 2,populationRange=4"

        + "&constraints=constraintId=2,required=true,importance=High,included=true"
        + "&constraints=constraintId=3,required=false,importance=Low,included=true"
        ;

        Form form = new Form(query);
        for (Parameter parameter : form) {
            System.out.print("===> parameter= " + parameter.getName());
            System.out.println(" / " + parameter.getValue());
        }
        List<Agent> agents = this.getAllAgents1(form.subList("agents"));

        List<Constraint> constraints = this.getAllConstraints1(form.subList("constraints"));

    }
    private List<Agent> getAllAgents1(Series<Parameter> inAgents) {

        List<Agent> agents = new ArrayList();

        //------------------------------------
        // 1) Get a list of all agents.
        // 2) Iterate thru each agent and indivually get that agent's params value pair.
        // 3) Iterate thru each param value pair, extract out the value and save it appropriately.
        //------------------------------------

        // 1) Get a list of all agents.
        Series<Parameter> agent = inAgents;

        // 2) Iterate thru each agent and indivually get that agent's params value pair.
        Iterator<Parameter> iter = agent.iterator();

        while (iter.hasNext()) {
            List params = new ArrayList();
            String values = iter.next().getValue();
            System.out.println("==> VALUES= " + values);

            Collections.addAll(params, values.split("\\,"));

            System.out.println("==> " + params.toString());

            // 3) Iterate thru each param value pair, extract out the value
            // and save it each param to a agent list object.

            Iterator<String> piter = params.iterator();
            Agent a = new Agent();
            while (piter.hasNext()) {
                String[] p = (piter.next()).split("\\=");
                System.out.println(p[0] + " --> " + p[1]);

                if (p[0].equalsIgnoreCase("filtered")) {
                    a.filtered = p[1];

                } else if (p[0].equalsIgnoreCase("population")) {
                    a.population = p[1];

                } else if (p[0].equalsIgnoreCase("populationRange")) {
                    a.populationRange = p[1];

                } else if (p[0].equalsIgnoreCase("population")) {
                    a.population = p[1];

                } else if (p[0].equalsIgnoreCase("table")) {
                    a.table = p[1];
                }
            }

            agents.add(a);

        }
        return agents;
    }

    private List<Constraint> getAllConstraints1(Series<Parameter> inConstrainsts) {

        List<Constraint> constraints = new ArrayList();

        //------------------------------------
        // 1) Get a list of all agents.
        // 2) Iterate thru each agent and indivually get that agent's params value pair.
        // 3) Iterate thru each param value pair, extract out the value and save it appropriately.
        //------------------------------------

        // 1) Get a list of all agents.
        Series<Parameter> constraint = inConstrainsts;

        // 2) Iterate thru each agent and indivually get that agent's params value pair.
        Iterator<Parameter> iter = constraint.iterator();

        while (iter.hasNext()) {
            List params = new ArrayList();
            String values = iter.next().getValue();
//            System.out.println("==> VALUES= " + values);

            Collections.addAll(params, values.split("\\,"));

            System.out.println("==> " + params.toString());

            // 3) Iterate thru each param value pair, extract out the value
            // and save it each param to a agent list object.

            Iterator<String> piter = params.iterator();
            Constraint a = new Constraint();
            while (piter.hasNext())
            {
                String[] p = (piter.next()).split("\\=");

                System.out.println(p[0] + " --> " + p[1]);

                if (p[0].equalsIgnoreCase("name")) {
                    a.name = p[1];

                } else if (p[0].equalsIgnoreCase("type")) {
                    a.type = p[1];

                } else if (p[0].equalsIgnoreCase("importance")) {
                    a.importance = p[1];

                } else if (p[0].equalsIgnoreCase("required")) {
                    a.required = p[1];

                } else if (p[0].equalsIgnoreCase("included")) {
                    a.included = p[1];
                }
            }

            constraints.add(a);

        }
        return constraints;
    }
    //=============================================================
    */

    @Test
    public void testRepresentation() {
        System.out.println("testRepresentation");

Pattern p = Pattern.compile("^(\\w+)\\[(\\w+)\\]\\[(\\d+)\\]$");
p = Pattern.compile("(.+)\\[(.+)\\]\\[(.+)\\]");
Matcher m;

        String query =
            "configId=000000000001&modelId=modid1&name=Name of the configuration"
                + "&startDate=2011-11-07 10:00&duration=20"
                + "&agents[0][id]=0001&agents[0][name]=0001&agents[0][filter]=on&agents[0][population]=5"
                + "&agents[1][id]=0001&agents[1][name]=0002&agents[1][population]=50"
                + "&agents[2][id]=0001&agents[2][name]=0003&agents[2][filter]=on&agents[2][population]=5000"
                + "&constraints[0][id]=0001&constraints[0][required]=on&constraints[0][importance]=Low"
                + "&constraints[1][id]=0002&constraints[1][importance]=Medium"
                + "&constraints[2][id]=0003&constraints[2][required]=on&constraints[2][importance]=High"
                ;

        Form form = new Form(query);

        /*
0: agents[0][id]
1: agents
2: 0
3: id
         *
         */
        List<Agent> agents = new ArrayList();


        for (Parameter parameter : form) {
            System.out.print("===> parameter= " + parameter.getName());
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
                   System.out.println("3: "+ m.group(3)); // "id"

                   Agent a = new Agent();
                   if (m.group(3).equalsIgnoreCase("filtered")) {
                       a.filtered = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("table")) {
                       a.table = parameter.getValue();
                   }
                   if (m.group(3).equalsIgnoreCase("population")) {
                       a.filtered = parameter.getValue();
                   }
                   agents.add(a);
                }
            } //end-if-agents
        }

    }

}

class SaveConfigurationParams {

    public String userId;
    public String token;
    public String modelId;
    public String configId;
    public Configuration configuration;
}
class Configuration {
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
}

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




//        String query =
//        "configuration=configId=000000000001&modelId=modid1&name=Name of the configuration&startDate=2011-11-07 10:00&duration=20"
//
//        + "&agents=agentId[0]=0001&filter[0]=on&population[0]=5"
//                + "&agentId[1]=0002&population[1]=1000"
//                + "&agentId[2]=0003&population[2]=50&filter[2]=on"
//
//        + "&constaints=configId[0]=343&constraintId[0]=7970"
//
//        + "&userId=1&token=cc385d8e-1345-4d16-8375-323c56bb2982"
//        ;




//        String query =
//        " http://localhost:8080/UniversalPortal/data/saveConfiguration.json?"
//        +"configuration=configId=000000000001&modelId=modid1&name=Name of the configuration&startDate=2011-11-07 10:00&duration=20"
//        + "&agents=agentId[0]=0001&filter[0]=on&population[0]=5"
//                + "&agentId[1]=0002&population[1]=1000"
//                + "&agentId[2]=0003&population[2]=50&filter[2]=on"
//        + "&constraints=constraintId[0]=0001&required[0]=on&importance[0]=5"
//                + "&constraintId[1]=0002&importance[1]=1000"
//                + "&constraintId[2]=0003&importance[2]=50&required[2]=on"
//        + "&userId=1&token=cc385d8e-1345-4d16-8375-323c56bb2982"
////        ;
//
//        List<Agent> agents = new ArrayList();
//        Agent a = new Agent();
//        a.filtered = "true";
//        a.population = "4";
//        agents.add(a);
//        a = new Agent();
//        a.filtered = "false";
//        a.population = "5";
//        a.table = "agent-table";
//        agents.add(a);
//
//        Configuration c = new Configuration();
//        c.name = "CONFIG 23";
//        c.agents = agents;
//        //c.constraints = constraints;
//
////        SaveConfigurationParams config = new SaveConfigurationParams();
////        config.userId = "1";
////        config.configuration = c;
//
//        Map<String,Object> attr = new HashMap<String, Object>();
//        attr.put("userId", "1");
//        attr.put("cnfiguration", c);
//
//        Request request = new Request();
//        request.setAttributes(attr);
//        request.setEntity(query, MediaType.ALL);
//
//        Representation rep = request.getEntity();
//        Form f = new Form(rep);
