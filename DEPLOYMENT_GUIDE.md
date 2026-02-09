# 🚀 Hodari Gardens: Fly.io Deployment Guide

This guide will take you from a fresh terminal to a live production website.

## 1. Install the Fly CLI
First, you need the tool that talks to Fly.io. Open your terminal and run:

**On Linux/macOS:**
```bash
curl -L https://fly.io/install.sh | sh
```

**On Windows (PowerShell):**
```powershell
iwr https://fly.io/install.ps1 -useb | iex
```

*Note: After installing, you might need to restart your terminal or add the fly directory to your PATH as instructed by the installer.*

---

## 2. Sign Up or Login
If you don't have an account, this will open a browser to let you sign up (they have a generous free tier).

```bash
fly auth signup
```

If you already have an account:
```bash
fly auth login
```

---

## 3. Navigate to your Project
Go to the folder where your code lives.

```bash
cd /home/ugodzilla/hodari2
```

---

## 4. Launch the App
Since I've already provided a `fly.toml` file, running this command will pick up those settings.

```bash
fly launch
```

**What to expect:**
- It will ask: `An existing fly.toml file was found. Would you like to copy its configuration to the new app?` -> Type **Y** and press Enter.
- It will ask for an **App Name** -> You can stick with `hodari-gardens` (if available) or choose something unique like `hodari-resort-nakuru`.
- It will ask for a **Region** -> Choose `nbi` (Nairobi) for the best performance in Kenya, or `jnb` (Johannesburg) if NBI isn't available.
- It will ask `Would you like to set up a Postgresql database?` -> Type **N** (we don't need one).
- It will ask `Would you like to set up an Upstash Redis database?` -> Type **N** (we don't need one).
- It will ask `Would you like to deploy now?` -> Type **Y**.

---

## 5. Verify the Deployment
Fly will now build your app (using the `Dockerfile` I made) and push it to their servers. This takes about 2-3 minutes.

Once it's done, you can open it in your browser with:
```bash
fly open
```

---

## 6. Keeping it Updated
Whenever you make changes to the code in the future and want to push them live, just run:

```bash
fly deploy
```

## 🛠 Troubleshooting
- **Build Fails?** Make sure you have Docker installed and running on your laptop, or Fly will try to build it in the cloud for you (which is fine).
- **Check Logs:** If the site doesn't load, see what the server is saying:
  ```bash
  fly logs
  ```

---
**Congratulations!** Your Hodari Gardens Resort platform is now live for the world to see. 🥂✨
