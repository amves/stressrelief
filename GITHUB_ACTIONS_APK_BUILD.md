# Building APK with GitHub Actions (No Android Studio Required!)

## 🎯 Overview

You can build APKs directly on GitHub without installing Android Studio! GitHub Actions will automatically build your app in the cloud every time you push code.

## ✅ What's Set Up

A GitHub Actions workflow (`.github/workflows/build.yml`) that:
- ✅ Automatically builds on every push to main/master/copilot branches
- ✅ Builds on every pull request
- ✅ Can be triggered manually anytime
- ✅ Builds both debug and release APKs
- ✅ Uploads APKs as downloadable artifacts
- ✅ Runs unit tests
- ✅ Caches dependencies for faster builds

## 🚀 How to Get Your APK

### Method 1: Automatic Build (Recommended)

1. **Push your code to GitHub**
   ```bash
   git push origin your-branch-name
   ```

2. **Go to GitHub Actions**
   - Navigate to your repository on GitHub
   - Click on the "Actions" tab at the top
   - You'll see a workflow running (orange dot) or completed (green checkmark)

3. **Download the APK**
   - Click on the completed workflow run
   - Scroll down to the "Artifacts" section
   - Download `app-debug` (this is the APK you can install)
   - The downloaded ZIP contains `app-debug.apk`

### Method 2: Manual Build

1. **Go to GitHub Actions tab**
2. **Click on "Android Build" workflow** (left sidebar)
3. **Click "Run workflow" button** (right side)
4. **Select your branch**
5. **Click green "Run workflow" button**
6. **Wait for build to complete** (~3-5 minutes)
7. **Download APK from Artifacts section**

## 📱 Installing the APK on Your Phone

### Option A: Direct Download on Phone

1. On your Android phone, open the GitHub Actions run page
2. Download the artifact ZIP
3. Extract `app-debug.apk` from the ZIP
4. Tap the APK file to install
5. Enable "Install from unknown sources" if prompted

### Option B: Transfer from Computer

1. Download artifact from GitHub Actions on your computer
2. Extract `app-debug.apk`
3. Transfer to phone via:
   - USB cable
   - Email attachment
   - Cloud storage (Google Drive, Dropbox)
   - Messaging app
4. Open APK on phone to install

## 🔍 Understanding the Workflow

### What Happens During Build?

```
1. Checkout Code ←────────────── Clones your repository
2. Setup JDK 17 ←────────────── Installs Java
3. Setup Android SDK ←─────────── Installs Android tools
4. Cache Dependencies ←────────── Speeds up future builds
5. Run Unit Tests ←────────────── Validates code quality
6. Build Debug APK ←───────────── Creates installable APK
7. Upload Artifact ←───────────── Makes APK downloadable
```

### Build Times

- **First build**: ~5-8 minutes (downloading dependencies)
- **Subsequent builds**: ~2-3 minutes (using cache)

### Artifacts Retention

APKs are kept for **30 days** after build. Download them before they expire!

## 🎨 Workflow Triggers

The workflow runs automatically on:

- ✅ Push to `main` branch
- ✅ Push to `master` branch  
- ✅ Push to any `copilot/*` branch
- ✅ Any pull request
- ✅ Manual trigger (workflow_dispatch)

## 📊 Viewing Build Status

### On GitHub

- Green checkmark ✅ = Build succeeded
- Red X ❌ = Build failed
- Orange dot 🟠 = Build in progress

### Build Badge (Optional)

Add this to your `README.md` to show build status:

```markdown
![Android Build](https://github.com/amves/stressrelief/workflows/Android%20Build/badge.svg)
```

## 🔧 Troubleshooting

### Build Failed?

1. **Click on the failed workflow run**
2. **Expand the failed step** to see error details
3. **Common issues:**
   - Missing dependencies: Check `build.gradle.kts`
   - Test failures: Fix failing tests or mark as `continue-on-error: true`
   - Google Services: Ensure `google-services.json` is committed

### APK Not Available?

- Check if the "Build debug APK" step succeeded
- Look in the "Artifacts" section at the bottom of the workflow run page
- Artifacts expire after 30 days

### Build Takes Too Long?

- First build is always slower (5-8 min)
- Enable Gradle caching (already configured)
- Consider using self-hosted runners for faster builds

## 🎯 Advanced: Release Builds

The workflow also builds release APKs (unsigned). To create signed release builds:

1. **Generate a keystore** locally or use existing one
2. **Add keystore to repository secrets:**
   - Settings → Secrets → Actions
   - Add: `KEYSTORE_FILE` (base64 encoded keystore)
   - Add: `KEYSTORE_PASSWORD`
   - Add: `KEY_ALIAS`
   - Add: `KEY_PASSWORD`
3. **Update workflow** to sign APK
4. **Build produces signed APK** ready for Play Store

## 💡 Tips & Best Practices

### Speed Up Builds

- ✅ Gradle caching is already enabled
- ✅ Dependencies are cached between runs
- ✅ Only clean build when necessary

### Save GitHub Actions Minutes

- Free tier: 2,000 minutes/month
- Each build uses ~3-5 minutes
- That's ~400-600 builds/month for free!

### Testing Before Installing

1. Download debug APK from Actions
2. Scan with VirusTotal if concerned
3. Install on test device first
4. Check app permissions before granting

## 📱 What's in the APK?

The debug APK includes all features:
- ✅ HRV Calculator (RMSSD, SDNN, PNN50)
- ✅ Health Connect integration
- ✅ Wear OS monitoring
- ✅ Firebase authentication
- ✅ Room database
- ✅ All UI screens
- ✅ Debug symbols (for troubleshooting)

## 🔗 Quick Links

- **Actions Tab**: `https://github.com/amves/stressrelief/actions`
- **Latest Workflow**: Click "Android Build" in left sidebar
- **Manual Run**: Actions → Android Build → Run workflow

## 🎓 Learning Resources

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Android Build with GitHub Actions](https://github.com/android/build-actions)
- [Gradle Build Cache](https://docs.gradle.org/current/userguide/build_cache.html)

---

## 🎉 Summary

**You never need to install Android Studio!**

1. Push code to GitHub
2. Wait 3-5 minutes
3. Download APK from Actions tab
4. Install on phone
5. Done! 🚀

The build happens entirely in the cloud on GitHub's servers. Just commit your code and download the APK!
