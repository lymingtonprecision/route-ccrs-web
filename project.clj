(defproject route-ccrs-web "1.0.0"
  :description "A HTTP service exposing the Route CCR calculation"
  :url "https://github.com/lymingtonprecision/route-ccrs-web"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]

                 ; components/system
                 [com.stuartsierra/component "0.2.3"]
                 [environ "1.0.0"]
                 [prismatic/plumbing "0.4.2"]

                 ; logging
                 [org.clojure/tools.logging "0.3.1"]
                 [org.spootnik/logconfig "0.7.3"]

                 ; database
                 [org.clojure/java.jdbc "0.3.6"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [hikari-cp "1.2.2"]

                 ; web
                 [http-kit "2.1.19"]
                 [ring/ring-core "1.0.0-RC1"]
                 [prismatic/fnhouse "0.1.1"]
                 [com.cognitect/transit-clj "0.8.271"]
                 [ring-transit "0.1.3"]

                 ; data processing
                 [prismatic/schema "0.4.0"]
                 [lymingtonprecision/route-ccrs "3.0.0"]]

  :main route-ccrs-web.main

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]}
             :uberjar {:aot [route-ccrs-web.main]}
             :repl {:source-paths ["dev" "src"]}}

  :repl-options {:init-ns user
                 :init (user/init)})
