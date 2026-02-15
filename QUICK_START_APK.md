# Quick Start: Get Your APK from GitHub Actions

## 🎯 3-Step Process

### Step 1: Push Your Code
```bash
git push origin your-branch-name
```

### Step 2: Go to GitHub Actions
1. Open your repository on GitHub: `https://github.com/amves/stressrelief`
2. Click the **"Actions"** tab at the top
3. You'll see your workflow running or completed

### Step 3: Download APK
1. Click on the latest workflow run (green ✓ means success)
2. Scroll to bottom → **"Artifacts"** section
3. Click **"app-debug"** to download
4. Unzip and install `app-debug.apk` on your phone

## 📱 Visual Guide

```
GitHub Repository
    ↓
[Actions] Tab ← Click here
    ↓
Latest Workflow Run ← Click the green checkmark
    ↓
Scroll to Bottom
    ↓
[Artifacts] Section
    ↓
[app-debug] ← Click to download ZIP
    ↓
Extract app-debug.apk
    ↓
Install on Phone! 🎉
```

## ⚡ Manual Trigger (Optional)

Don't want to push? Trigger build manually:

1. Go to Actions tab
2. Click "Android Build" (left sidebar)
3. Click "Run workflow" button (right side)
4. Select branch → Click green "Run workflow"
5. Wait 3-5 minutes → Download APK

## 🔗 Direct Links

- **Actions Page**: https://github.com/amves/stressrelief/actions
- **Workflow**: https://github.com/amves/stressrelief/actions/workflows/build.yml

## ⏱️ Build Time

- First build: ~5-8 minutes
- Later builds: ~2-3 minutes (cached)

## 💾 Storage

APKs are stored for **30 days**. Download before they expire!

## ❓ Troubleshooting

**No APK available?**
- Wait for build to finish (green checkmark)
- Check "Build debug APK" step succeeded
- Look in Artifacts section at bottom

**Build failed?**
- Click the red X to see error
- Check logs for specific error
- Common: missing dependencies or test failures

**Can't find Artifacts?**
- Must be logged into GitHub
- Scroll all the way to bottom of workflow page
- Artifacts section is below all the steps

---

**Need more help?** See full guide: [GITHUB_ACTIONS_APK_BUILD.md](GITHUB_ACTIONS_APK_BUILD.md)
