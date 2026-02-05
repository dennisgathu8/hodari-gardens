(ns hodari-gardens.components.about
  "About section component.")

(defn about-section
  "About page content."
  []
  [:section.py-16.bg-white.dark:bg-gray-900
   [:div.max-w-4xl.mx-auto.px-4.sm:px-6.lg:px-8
    [:div.text-center.mb-12
     [:h1.section-title "About Hodari Gardens"]
     [:p.section-subtitle
      "Your Premier Destination in Nakuru"]]
    
    [:div.prose.prose-lg.dark:prose-invert.max-w-none
     [:div.mb-8
      [:h2.text-3xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
       "Our Story"]
      [:p.text-gray-600.dark:text-gray-300.leading-relaxed
       "Hodari Gardens Resort is Nakuru's premier destination for luxury accommodation, events, and entertainment. Located in the serene area of Nakuru Lanet, near Nakuru Specialist Hospital, we offer a perfect blend of natural beauty and modern amenities."]
      [:p.text-gray-600.dark:text-gray-300.leading-relaxed.mt-4
       "Our resort features beautifully landscaped gardens that provide an ideal setting for weddings, corporate events, and social gatherings. Whether you're looking for a peaceful getaway, planning a special event, or wanting to watch major sporting events with friends, Hodari Gardens is your perfect choice."]]
     
     [:div.mb-8
      [:h2.text-3xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
       "Our Facilities"]
      [:ul.space-y-3.text-gray-600.dark:text-gray-300
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Luxury Accommodation:"] " Comfortable rooms ranging from Standard to Executive Suites"]]
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Expansive Gardens:"] " Beautifully maintained gardens perfect for events and relaxation"]]
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Bar & Lounge:"] " Fully stocked bar with a wide selection of drinks and beverages"]]
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Sports Viewing:"] " Multiple big screens for watching major sporting events including World Cup 2026"]]
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Event Spaces:"] " Versatile venues for weddings, corporate functions, and social events"]]
       [:li.flex.items-start
        [:svg.w-6.h-6.mr-3.text-garden-green-600.flex-shrink-0.mt-1 {:xmlns "http://www.w3.org/2000/svg" :viewBox "0 0 20 20" :fill "currentColor"}
         [:path {:fill-rule "evenodd" :d "M10 18a8 8 0 100-16 8 8 0 000 16zm3.707-9.293a1 1 0 00-1.414-1.414L9 10.586 7.707 9.293a1 1 0 00-1.414 1.414l2 2a1 1 0 001.414 0l4-4z" :clip-rule "evenodd"}]]
        [:span [:strong "Ample Parking:"] " Secure parking space for all guests"]]]]
     
     [:div.mb-8
      [:h2.text-3xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
       "Location Advantage"]
      [:p.text-gray-600.dark:text-gray-300.leading-relaxed
       "Strategically located in Nakuru Lanet, we are just minutes away from Nakuru town center and conveniently situated near Nakuru Specialist Hospital. This makes us an ideal choice for medical tourists and their families, as well as business travelers and leisure guests."]
      [:p.text-gray-600.dark:text-gray-300.leading-relaxed.mt-4
       "Our proximity to major attractions like Lake Nakuru National Park and Menengai Crater makes us the perfect base for exploring the beauty of Nakuru County."]]
     
     [:div.mb-8
      [:h2.text-3xl.font-serif.font-bold.mb-4.text-gray-900.dark:text-white
       "Why Choose Us"]
      [:div.grid.grid-cols-1.md:grid-cols-2.gap-6.mt-6
       [:div.bg-garden-green-50.dark:bg-garden-green-900.p-6.rounded-lg
        [:h3.font-bold.text-lg.mb-2.text-gray-900.dark:text-white "Serene Environment"]
        [:p.text-gray-600.dark:text-gray-300.text-sm
         "Escape the hustle and bustle in our peaceful garden setting"]]
       [:div.bg-garden-green-50.dark:bg-garden-green-900.p-6.rounded-lg
        [:h3.font-bold.text-lg.mb-2.text-gray-900.dark:text-white "Professional Service"]
        [:p.text-gray-600.dark:text-gray-300.text-sm
         "Our dedicated team ensures your comfort and satisfaction"]]
       [:div.bg-garden-green-50.dark:bg-garden-green-900.p-6.rounded-lg
        [:h3.font-bold.text-lg.mb-2.text-gray-900.dark:text-white "Modern Amenities"]
        [:p.text-gray-600.dark:text-gray-300.text-sm
         "Enjoy contemporary facilities in a natural setting"]]
       [:div.bg-garden-green-50.dark:bg-garden-green-900.p-6.rounded-lg
        [:h3.font-bold.text-lg.mb-2.text-gray-900.dark:text-white "Competitive Pricing"]
        [:p.text-gray-600.dark:text-gray-300.text-sm
         "Quality service and accommodation at affordable rates"]]]]
     
     [:div.text-center.mt-12
      [:a.btn-primary.inline-block
       {:href "#contact"}
       "Get In Touch"]]]]])
