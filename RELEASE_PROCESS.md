# Release Process Guide

## Overview

This document describes how to create official releases of the StressRelief app using git tags.

## 🎯 Release Flow

```
1. Developer/Maintainer decides to release
   ↓
2. Create and push a git tag (e.g., v1.0.0)
   ↓
3. GitHub Actions automatically:
   - Builds APKs with version from tag
   - Runs all tests
   - Generates changelog
   - Creates GitHub Release
   - Uploads APKs to release
   - Uploads to Google Play Store (if configured)
   ↓
4. Release is published and available for download
   ↓
5. Promote in Google Play Console (if uploaded)
```

## 📋 How to Create a Release

### Step 1: Prepare for Release

1. **Ensure all changes are merged to main/master**
   ```bash
   git checkout main
   git pull origin main
   ```

2. **Run tests locally** (optional but recommended)
   ```bash
   ./gradlew test
   ```

3. **Verify the app works**
   - Build locally or from GitHub Actions
   - Test on a device

### Step 2: Create a Git Tag

**Choose your version number** following [Semantic Versioning](https://semver.org/):
- **MAJOR.MINOR.PATCH** (e.g., 1.0.0, 1.2.3, 2.0.0)
- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes

**Create the tag:**

```bash
# Format: v{MAJOR}.{MINOR}.{PATCH}
git tag v1.0.0

# Or with a message
git tag -a v1.0.0 -m "Release version 1.0.0"
```

### Step 3: Push the Tag

```bash
git push origin v1.0.0
```

**That's it!** GitHub Actions will automatically:
- Detect the new tag
- Trigger the release workflow
- Build the APKs
- Create the release
- Publish everything

### Step 4: Monitor the Build

1. Go to GitHub Actions: https://github.com/amves/stressrelief/actions
2. Look for "Release Build" workflow
3. Watch it build (takes ~3-5 minutes)
4. Check for any errors

### Step 5: Verify the Release

1. Go to Releases: https://github.com/amves/stressrelief/releases
2. Find your new release
3. Verify APKs are attached
4. Download and test if desired

## 📦 What Gets Published

For each release, the following are created:

### GitHub Release
- **Tag**: The git tag (e.g., v1.0.0)
- **Title**: "StressRelief v{VERSION}"
- **Description**: Auto-generated changelog from commits
- **Assets**: 2 APK files

### APK Files
1. **stressrelief-{VERSION}-release.apk**
   - Release build (signed if keystore configured)
   - Optimized for production
   - Smaller file size

2. **stressrelief-{VERSION}-debug.apk**
   - Debug build
   - Includes debug symbols
   - Easier to troubleshoot

### Google Play Store (If Configured)
- **Track**: Internal testing track
- **Status**: Released (ready for promotion)
- **Promotion**: Manually promote to alpha/beta/production

📖 **Setup Guide**: See [GOOGLE_PLAY_PUBLISHING.md](GOOGLE_PLAY_PUBLISHING.md) for configuration details.

## 🏷️ Version Numbering

### Automatic Version Code Calculation

The version code is automatically calculated from the tag:
```
Version Code = MAJOR * 10000 + MINOR * 100 + PATCH

Examples:
v1.0.0  → 10000
v1.2.3  → 10203
v2.0.0  → 20000
v1.15.7 → 11507
```

This ensures:
- Each version has a unique code
- Versions are properly ordered
- Play Store requirements are met

### Tag Formats Supported

Both formats work:
- `v1.0.0` (with 'v' prefix) ✅
- `1.0.0` (without 'v' prefix) ✅

## 📝 Examples

### Example 1: Initial Release

```bash
# First release of the app
git tag v1.0.0
git push origin v1.0.0

# Result:
# - Version: 1.0.0
# - Version Code: 10000
# - APKs: stressrelief-1.0.0-release.apk, stressrelief-1.0.0-debug.apk
```

### Example 2: Bug Fix Release

```bash
# Fixed some bugs
git tag v1.0.1
git push origin v1.0.1

# Result:
# - Version: 1.0.1
# - Version Code: 10001
```

### Example 3: Feature Release

```bash
# Added new features
git tag v1.1.0
git push origin v1.1.0

# Result:
# - Version: 1.1.0
# - Version Code: 10100
```

### Example 4: Major Release

```bash
# Breaking changes or major milestone
git tag v2.0.0
git push origin v2.0.0

# Result:
# - Version: 2.0.0
# - Version Code: 20000
```

## 🔧 Advanced: Annotated Tags with Release Notes

You can add custom release notes when creating the tag:

```bash
git tag -a v1.0.0 -m "Release 1.0.0

Features:
- HRV calculation with Health Connect
- Wear OS heart rate monitoring
- Firebase authentication
- Dashboard analytics

Bug Fixes:
- Fixed login issue
- Improved performance

Known Issues:
- None"

git push origin v1.0.0
```

The tag message will be included in the changelog.

## 🚨 Important Notes

### DO:
✅ Create tags from main/master branch  
✅ Use semantic versioning (MAJOR.MINOR.PATCH)  
✅ Test before tagging  
✅ Include meaningful commit messages (they become changelog)  
✅ Verify the release after it's published  

### DON'T:
❌ Create tags from feature branches  
❌ Reuse or delete tags after pushing  
❌ Skip version numbers  
❌ Use arbitrary version schemes  
❌ Tag untested code  

## 🔄 Managing Tags

### List All Tags
```bash
git tag
# or
git tag -l "v*"
```

### Delete a Local Tag
```bash
git tag -d v1.0.0
```

### Delete a Remote Tag (use carefully!)
```bash
git push --delete origin v1.0.0
```

**Note**: Deleting a pushed tag won't delete the GitHub Release. You'll need to delete that manually.

### View Tag Details
```bash
git show v1.0.0
```

## 📊 Versioning Strategy

### Recommended Approach

1. **Start with v1.0.0** for first public release
2. **Increment PATCH** for bug fixes: v1.0.1, v1.0.2
3. **Increment MINOR** for new features: v1.1.0, v1.2.0
4. **Increment MAJOR** for breaking changes: v2.0.0

### Pre-releases (Optional)

For beta versions or release candidates:
```bash
git tag v1.0.0-beta.1
git tag v1.0.0-rc.1
```

The workflow supports these too!

## 🎯 Release Checklist

Before creating a release:

- [ ] All features tested and working
- [ ] All tests passing
- [ ] README.md updated
- [ ] CHANGELOG.md updated (if you maintain one)
- [ ] Version numbers make sense
- [ ] No known critical bugs
- [ ] Commits have clear messages
- [ ] Code is on main/master branch
- [ ] Latest commits pulled locally

## 📱 Installing a Release

Users can install releases by:

1. **Go to Releases page**: https://github.com/amves/stressrelief/releases
2. **Download latest APK**: Click on `stressrelief-X.X.X-release.apk`
3. **Transfer to Android device**
4. **Enable "Install from unknown sources"**
5. **Tap APK to install**

## 🔐 Signing Releases (Optional)

Currently, releases are unsigned. To sign them:

1. **Generate a keystore**:
   ```bash
   keytool -genkey -v -keystore release.keystore -alias stressrelief -keyalg RSA -keysize 2048 -validity 10000
   ```

2. **Add to GitHub Secrets**:
   - `KEYSTORE_FILE`: Base64 encoded keystore
   - `KEYSTORE_PASSWORD`: Keystore password
   - `KEY_ALIAS`: Key alias
   - `KEY_PASSWORD`: Key password

3. **Update release.yml** workflow to sign APKs

See [Android documentation](https://developer.android.com/studio/publish/app-signing) for details.

## 🆘 Troubleshooting

### Release workflow didn't trigger
- Verify tag was pushed: `git ls-remote --tags origin`
- Check tag format matches pattern (v*.*.*)
- Look at Actions tab for any failed runs

### Build failed during release
- Check Actions tab for error logs
- Verify tests pass locally
- Check if version format is correct

### APK not in release
- Workflow must complete successfully
- Check "Build release APK" step in Actions
- Verify file paths in workflow are correct

### Wrong version in APK
- Tag format must be correct (MAJOR.MINOR.PATCH)
- Check workflow logs for version extraction
- Verify build.gradle.kts was updated correctly

## 📚 Additional Resources

- [Semantic Versioning](https://semver.org/)
- [Git Tagging](https://git-scm.com/book/en/v2/Git-Basics-Tagging)
- [GitHub Releases](https://docs.github.com/en/repositories/releasing-projects-on-github)
- [Android App Signing](https://developer.android.com/studio/publish/app-signing)
- [Google Play Publishing Setup](GOOGLE_PLAY_PUBLISHING.md)

## 🏪 Publishing to Google Play Store

Want to automatically upload releases to Google Play Store?

### Quick Overview

When configured, the release workflow automatically:
1. ✅ Builds a signed APK
2. ✅ Uploads to Google Play Console (Internal Track)
3. ✅ Makes release available for promotion

### Setup Required

You'll need to configure 5 GitHub Secrets:
1. **KEYSTORE_BASE64** - Your app signing keystore (base64 encoded)
2. **KEYSTORE_PASSWORD** - Keystore password
3. **KEY_ALIAS** - Key alias name
4. **KEY_PASSWORD** - Key password
5. **PLAY_STORE_SERVICE_ACCOUNT_JSON** - Google Play API credentials

### After Upload

Once uploaded to the Internal track, you can:
- Promote to **Closed Testing** (alpha/beta)
- Promote to **Open Testing** (public beta)
- Promote to **Production** (all users)

### Full Documentation

📖 **Complete Setup Guide**: [GOOGLE_PLAY_PUBLISHING.md](GOOGLE_PLAY_PUBLISHING.md)

The guide covers:
- Creating a signing keystore
- Setting up Google Play service account
- Adding secrets to GitHub
- Troubleshooting common issues
- Best practices for security

## 🎉 Summary

**To create a release:**
```bash
git tag v1.0.0
git push origin v1.0.0
```

**Wait 3-5 minutes, then download from:**
https://github.com/amves/stressrelief/releases

That's it! The workflow handles everything else automatically.
