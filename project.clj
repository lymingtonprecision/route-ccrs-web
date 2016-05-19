(defproject route-ccrs-web "1.2.1-SNAPSHOT"
  :description "A HTTP service exposing the Route CCR calculation"
  :url "https://github.com/lymingtonprecision/route-ccrs-web"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]

                 ; components/system
                 [com.stuartsierra/component "0.3.1"]
                 [environ "1.0.3"]
                 [prismatic/plumbing "0.5.3"]

                 ; logging
                 [org.clojure/tools.logging "0.3.1"]
                 [org.spootnik/logconfig "0.7.3"]

                 ; database
                 [org.clojure/java.jdbc "0.6.1"]
                 [org.clojars.zentrope/ojdbc "11.2.0.3.0"]
                 [hikari-cp "1.6.1"]

                 ; web
                 [http-kit "2.1.19"]
                 [ring/ring-core "1.4.0"
                  :exclusions [clj-time]]
                 [prismatic/fnhouse "0.2.1"]
                 [com.cognitect/transit-clj "0.8.285"]
                 [ring-transit "0.1.4"]

                 ; data processing
                 [prismatic/schema "1.1.1"]
                 [lymingtonprecision/route-ccrs "3.4.0"]]

  :main route-ccrs-web.main

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.10"]]}
             :uberjar {:aot [route-ccrs-web.main]}
             :repl {:source-paths ["dev" "src"]}}

  :repl-options {:init-ns user
                 :init (user/init)})
