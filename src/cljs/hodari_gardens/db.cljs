(ns hodari-gardens.db
  "Application database schema and default state.")

(def default-db
  "Default application state structure.
   Contains all client-side state including data, UI state, and user preferences."
  {:current-route nil
   :dark-mode? false
   :mobile-menu-open? false
   :loading? false
   :error nil
   
   ;; Data from API
   :rooms []
   :events {:event-packages []
            :gallery []}
   :drinks {:menu {}
            :happy-hours []}
   :worldcup {:tournament {}
              :matches []
              :viewing-packages []
              :facilities {}}
   :contact {}
   
   ;; UI state
   :selected-room nil
   :selected-event nil
   :lightbox {:open? false
              :current-image nil
              :images []}
   :filters {:event-type :all
             :match-stage :all}
   
   ;; Form state
   :booking-form {:name ""
                  :email ""
                  :phone ""
                  :check-in ""
                  :check-out ""
                  :room-type ""
                  :guests 1
                  :special-requests ""}
   :inquiry-form {:name ""
                  :email ""
                  :phone ""
                  :subject ""
                  :message ""}
   :form-errors {}
   :form-submitting? false})
