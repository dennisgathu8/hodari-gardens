# Hodari Gardens Resort - Full-Stack Web Application

A production-ready, responsive website for **Hodari Gardens Resort** in Nakuru Lanet, Kenya. Built with Clojure/ClojureScript using modern web development best practices.

## 🌟 Features

- **Luxury Accommodation** - Browse and book rooms (Standard, Deluxe, Executive Suite)
- **Drinks & Dining** - Full menu with cocktails, beers, wines, and soft drinks
- **Events & Gardens** - Wedding and corporate event packages with photo gallery
- **World Cup 2026** - Match schedule, countdown timer, and viewing packages
- **Contact & Location** - Interactive forms, map integration, and contact information
- **Dark Mode** - Toggle between light and dark themes
- **Responsive Design** - Mobile-first design that works on all devices
- **Accessibility** - WCAG 2.1 AA compliant with semantic HTML and ARIA labels

## 🛠️ Technology Stack

### Backend
- **Clojure** (JVM) - Server-side logic
- **Ring** - HTTP server abstraction
- **Reitit** - Fast, data-driven routing
- **Jetty** - Web server

### Frontend
- **ClojureScript** - Client-side logic
- **Reagent** - React wrapper for ClojureScript
- **Re-frame** - State management framework
- **Shadow-CLJS** - Build tool and development environment
- **Tailwind CSS** - Utility-first CSS framework
- **Bidi & Pushy** - Client-side routing

### Data
- **EDN** - Extensible Data Notation for configuration and data storage

## 📋 Prerequisites

- **Java JDK** 11 or higher
- **Clojure CLI** tools (1.11.1 or higher)
- **Node.js** 16+ and npm (for Shadow-CLJS and Tailwind)

## 🚀 Quick Start

### 1. Install Dependencies

```bash
# Install Node.js dependencies
npm install

# Clojure dependencies are managed by deps.edn and will be downloaded automatically
```

### 2. Development Mode

Start the backend server and frontend development server:

```bash
# Terminal 1: Start the backend server
clj -M:dev -m hodari-gardens.server

# Terminal 2: Start Shadow-CLJS watch (frontend)
npm run dev
```

The application will be available at:
- **Frontend**: http://localhost:8280
- **Backend API**: http://localhost:3000

### 3. Build for Production

```bash
# Build optimized frontend bundle
npm run build

# Create uberjar (includes compiled frontend assets)
clj -T:build uber
```

## 📁 Project Structure

```
hodari-gardens/
├── deps.edn                    # Clojure dependencies
├── shadow-cljs.edn             # Shadow-CLJS configuration
├── package.json                # Node.js dependencies
├── tailwind.config.js          # Tailwind CSS configuration
├── README.md                   # This file
│
├── resources/
│   ├── data/                   # EDN data files
│   │   ├── rooms.edn          # Room inventory
│   │   ├── events.edn         # Event packages and gallery
│   │   ├── drinks.edn         # Drinks menu
│   │   ├── worldcup.edn       # World Cup data
│   │   └── contact.edn        # Contact information
│   │
│   └── public/                 # Static assets
│       ├── index.html         # Main HTML file
│       ├── css/
│       │   └── styles.css     # Tailwind CSS
│       └── js/                # Compiled ClojureScript (generated)
│
├── src/
│   ├── clj/hodari_gardens/    # Backend (Clojure)
│   │   ├── server.clj         # Main server
│   │   └── api.clj            # API handlers
│   │
│   └── cljs/hodari_gardens/   # Frontend (ClojureScript)
│       ├── core.cljs          # Application entry point
│       ├── db.cljs            # Application state schema
│       ├── events.cljs        # Re-frame event handlers
│       ├── subs.cljs          # Re-frame subscriptions
│       ├── routes.cljs        # Client-side routing
│       ├── views.cljs         # Main view components
│       │
│       └── components/        # UI components
│           ├── navigation.cljs
│           ├── hero.cljs
│           ├── accommodation.cljs
│           ├── drinks.cljs
│           ├── events.cljs
│           ├── worldcup.cljs
│           ├── contact.cljs
│           ├── footer.cljs
│           └── about.cljs
│
└── test/                       # Test files
```

## 🔌 API Endpoints

### Rooms
- `GET /api/rooms` - Get all rooms
- `GET /api/rooms/:id` - Get specific room

### Events
- `GET /api/events` - Get all event packages and gallery
- `GET /api/events/:id` - Get specific event package

### Drinks
- `GET /api/drinks` - Get drinks menu and happy hours

### World Cup
- `GET /api/worldcup` - Get complete World Cup data
- `GET /api/worldcup/matches` - Get matches (filterable by stage/date)

### Contact
- `GET /api/contact` - Get contact information

### Forms
- `POST /api/booking` - Submit accommodation booking
- `POST /api/inquiry` - Submit general inquiry

## 🎨 Customization

### Updating Data

All content is stored in EDN files under [`resources/data/`](resources/data/). Edit these files to update:
- Room details and pricing
- Event packages
- Drinks menu
- World Cup schedule
- Contact information

### Styling

The application uses Tailwind CSS with custom color schemes:
- **Garden Green** - Primary brand color
- **Resort Gold** - Accent color

Modify [`tailwind.config.js`](tailwind.config.js) to customize colors, fonts, and other design tokens.

### Adding New Pages

1. Add route to [`src/cljs/hodari_gardens/routes.cljs`](src/cljs/hodari_gardens/routes.cljs)
2. Create component in [`src/cljs/hodari_gardens/components/`](src/cljs/hodari_gardens/components/)
3. Add page function to [`src/cljs/hodari_gardens/views.cljs`](src/cljs/hodari_gardens/views.cljs)
4. Update navigation in [`src/cljs/hodari_gardens/components/navigation.cljs`](src/cljs/hodari_gardens/components/navigation.cljs)

## 🧪 Testing

```bash
# Run Clojure tests
clj -M:test

# Run ClojureScript tests
npm run test
```

## 📦 Deployment

### Option 1: Uberjar

```bash
# Build uberjar
clj -T:build uber

# Run uberjar
java -jar target/hodari-gardens.jar
```

### Option 2: Docker

```bash
# Build Docker image
docker build -t hodari-gardens .

# Run container
docker run -p 3000:3000 hodari-gardens
```

### Environment Variables

- `PORT` - Server port (default: 3000)

## ♿ Accessibility

This application follows WCAG 2.1 AA guidelines:
- Semantic HTML5 structure
- ARIA labels and roles
- Keyboard navigation support
- Color contrast ratios meet AA standards
- Screen reader friendly
- Focus indicators on interactive elements

## 🌐 Browser Support

- Chrome/Edge (latest 2 versions)
- Firefox (latest 2 versions)
- Safari (latest 2 versions)
- Mobile browsers (iOS Safari, Chrome Mobile)

## 📄 License

Copyright © 2026 Hodari Gardens Resort. All rights reserved.

## 🤝 Contributing

This is a private project for Hodari Gardens Resort. For inquiries, contact:
- Email: info@hodarigardens.co.ke
- Phone: +254 712 345 678

## 🐛 Troubleshooting

### Shadow-CLJS won't start
```bash
# Clear cache and restart
rm -rf .shadow-cljs
npm run dev
```

### Backend API errors
```bash
# Check if port 3000 is available
lsof -i :3000

# Restart server
clj -M:dev -m hodari-gardens.server
```

### Tailwind styles not applying
```bash
# Rebuild CSS
npx tailwindcss -i ./resources/public/css/styles.css -o ./resources/public/css/output.css --watch
```

## 📞 Support

For technical support or questions:
- Email: dev@hodarigardens.co.ke
- Documentation: See inline code comments

---

**Built with ❤️ using Clojure & ClojureScript**
