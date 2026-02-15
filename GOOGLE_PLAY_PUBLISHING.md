# Google Play Store Publishing Guide

## 🎯 Overview

This guide explains how to automatically publish your APK releases to Google Play Store directly from GitHub Actions. When you create a git tag, the release workflow will:

1. ✅ Build a signed release APK
2. ✅ Upload to Google Play Console (Internal Track)
3. ✅ Create a GitHub Release with downloadable APKs

## 📋 Prerequisites

Before you can publish to Google Play Store, you need:

1. **Google Play Developer Account** ($25 one-time fee)
   - Sign up at: https://play.google.com/console
   
2. **App created in Google Play Console**
   - Your app must be registered with package name `com.amves.stressrelief`
   - Complete app details, screenshots, and privacy policy
   
3. **Service Account for API Access**
   - Needed for automated publishing via GitHub Actions
   
4. **App Signing Keystore**
   - Required to sign release builds

## 🔐 Step 1: Create a Signing Keystore

### Generate a New Keystore

If you don't have a keystore yet, create one:

```bash
keytool -genkey -v -keystore release.keystore \
  -alias stressrelief \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

**Important**: 
- Keep this file **SECURE** and **BACKED UP**
- You cannot update your app without this keystore
- Store it in a password manager or secure location

### Keystore Information to Remember

During generation, you'll set:
- **Keystore password**: Password for the keystore file
- **Key password**: Password for the app signing key
- **Key alias**: Name you gave the key (e.g., "stressrelief")

Write these down securely - you'll need them for GitHub Secrets.

## 🔑 Step 2: Create Google Play Service Account

### 2.1 Enable Google Play Developer API

1. Go to [Google Play Console](https://play.google.com/console)
2. Select your app
3. Navigate to **Settings** → **API Access**
4. Click **Enable API Access** (if not already enabled)

### 2.2 Create Service Account

1. Click **Create new service account**
2. Follow the link to Google Cloud Console
3. In Google Cloud Console:
   - Click **+ CREATE SERVICE ACCOUNT**
   - Name: `github-actions-publisher`
   - Description: `Service account for publishing from GitHub Actions`
   - Click **CREATE AND CONTINUE**
   
### 2.3 Grant Permissions

1. In the service account creation wizard:
   - Skip "Grant this service account access to project" (click CONTINUE)
   - Skip "Grant users access to this service account" (click DONE)

2. Back in the service account list:
   - Click on your new service account email
   - Go to **KEYS** tab
   - Click **ADD KEY** → **Create new key**
   - Choose **JSON** format
   - Click **CREATE**
   - A JSON file will download - **keep this secure!**

### 2.4 Grant Play Console Permissions

1. Go back to [Play Console API Access page](https://play.google.com/console)
2. Find your service account in the list
3. Click **Grant Access**
4. Set permissions:
   - **Account permissions**: None needed
   - **App permissions**: 
     - Select your app
     - Set **Release manager** role (allows uploading and managing releases)
5. Click **Apply**
6. Click **Invite user**

## 🔒 Step 3: Add Secrets to GitHub

Now you'll add the sensitive information to GitHub Secrets so the workflow can use them securely.

### 3.1 Navigate to Repository Secrets

1. Go to your GitHub repository: https://github.com/amves/stressrelief
2. Click **Settings** tab
3. In left sidebar, click **Secrets and variables** → **Actions**
4. Click **New repository secret**

### 3.2 Add Required Secrets

Add each of these secrets:

#### Secret 1: KEYSTORE_BASE64

Your keystore file, encoded in base64:

```bash
# Encode your keystore
base64 -i release.keystore | tr -d '\n' > keystore.txt

# Copy the content of keystore.txt
cat keystore.txt
```

- **Name**: `KEYSTORE_BASE64`
- **Value**: Paste the base64 string

#### Secret 2: KEYSTORE_PASSWORD

- **Name**: `KEYSTORE_PASSWORD`
- **Value**: Your keystore password (from Step 1)

#### Secret 3: KEY_ALIAS

- **Name**: `KEY_ALIAS`
- **Value**: Your key alias (e.g., `stressrelief`)

#### Secret 4: KEY_PASSWORD

- **Name**: `KEY_PASSWORD`
- **Value**: Your key password (from Step 1)

#### Secret 5: PLAY_STORE_SERVICE_ACCOUNT_JSON

Your service account JSON file content:

```bash
# Copy the content of your service account JSON
cat ~/Downloads/your-project-123456-abc123def456.json
```

- **Name**: `PLAY_STORE_SERVICE_ACCOUNT_JSON`
- **Value**: Paste the entire JSON content

### 3.3 Verify Secrets

After adding all secrets, you should have:
- ✅ KEYSTORE_BASE64
- ✅ KEYSTORE_PASSWORD
- ✅ KEY_ALIAS
- ✅ KEY_PASSWORD
- ✅ PLAY_STORE_SERVICE_ACCOUNT_JSON

## 🚀 Step 4: Publishing a Release

### Automatic Publishing

Once secrets are configured, releases happen automatically:

```bash
# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

The workflow will:
1. ✅ Build a signed APK
2. ✅ Upload to Google Play (Internal Track)
3. ✅ Create GitHub Release

### Monitor the Release

1. Go to [GitHub Actions](https://github.com/amves/stressrelief/actions)
2. Find the "Release Build" workflow
3. Watch the progress
4. Check for "Publish to Google Play Store" step

## 📱 Step 5: Promote in Play Console

After the automated upload:

1. Go to [Google Play Console](https://play.google.com/console)
2. Select your app
3. Navigate to **Release** → **Testing** → **Internal testing**
4. You'll see your uploaded release
5. Review and promote to:
   - **Closed testing** (alpha/beta testers)
   - **Open testing** (public beta)
   - **Production** (all users)

## 🎯 Publishing Tracks Explained

### Internal Track (Default)
- **Automatic**: GitHub Actions uploads here
- **Purpose**: Quick testing with up to 100 testers
- **No review**: Releases instantly
- **Use for**: Internal QA, immediate testing

### Closed Testing (Alpha/Beta)
- **Manual**: Promote from Internal
- **Purpose**: Testing with selected users
- **No review**: Usually instant
- **Use for**: Beta testers, early adopters

### Open Testing (Beta)
- **Manual**: Promote from Closed/Internal
- **Purpose**: Public beta testing
- **Review**: May require Google review
- **Use for**: Public beta program

### Production
- **Manual**: Promote from any track
- **Purpose**: All users
- **Review**: Requires Google review (24-48 hours)
- **Use for**: Official public releases

## 🔧 Configuration Options

### Change Publishing Track

Edit `.github/workflows/release.yml`:

```yaml
- name: Publish to Google Play Store
  run: ./gradlew publishReleaseBundle --track=beta
```

Or update `app/build.gradle.kts`:

```kotlin
play {
    track.set("beta")  // Options: internal, alpha, beta, production
}
```

### Publish App Bundle Instead of APK

App Bundles are recommended by Google (smaller downloads):

In `app/build.gradle.kts`:
```kotlin
play {
    defaultToAppBundles.set(true)
}
```

In workflow:
```yaml
- name: Build release bundle
  run: ./gradlew bundleRelease

- name: Publish to Google Play Store
  run: ./gradlew publishReleaseBundle
```

### Release Notes

Create release notes automatically:

In `app/src/main/play/release-notes/en-US/default.txt`:
```
• New features in this release
• Bug fixes and improvements
• Performance enhancements
```

Or set dynamically in workflow:
```yaml
- name: Create release notes
  run: |
    mkdir -p app/src/main/play/release-notes/en-US
    echo "${{ steps.changelog.outputs.changelog }}" > app/src/main/play/release-notes/en-US/default.txt
```

## 🛡️ Security Best Practices

### ✅ DO:
- Keep keystore file backed up securely
- Use strong passwords for keystore
- Store secrets only in GitHub Secrets
- Use service account with minimal permissions
- Enable two-factor authentication on Play Console
- Review service account activity regularly

### ❌ DON'T:
- Commit keystore files to Git
- Share keystore passwords via email/chat
- Use the same keystore for multiple apps
- Give service accounts unnecessary permissions
- Store secrets in environment variables or config files

## 🐛 Troubleshooting

### "Service Account doesn't have permissions"

**Solution**: 
1. Go to Play Console → API Access
2. Find your service account
3. Click "Grant Access"
4. Ensure "Release manager" role is selected

### "Package name mismatch"

**Solution**: 
Ensure your app's package name in Play Console matches `com.amves.stressrelief` in `app/build.gradle.kts`

### "Invalid signing configuration"

**Solution**: 
1. Verify KEYSTORE_BASE64 secret is correct
2. Check KEYSTORE_PASSWORD, KEY_ALIAS, KEY_PASSWORD
3. Ensure base64 encoding has no line breaks (`tr -d '\n'`)

### "Version code must be greater than previous"

**Solution**: 
Your version code is automatically calculated from the tag. Ensure your new tag version is higher than the previous one:
- v1.0.0 → Version Code: 10000
- v1.0.1 → Version Code: 10001
- v1.1.0 → Version Code: 10100

### "APK upload failed"

**Solution**: 
Check GitHub Actions logs for specific error. Common issues:
1. Service account credentials expired
2. App not properly created in Play Console
3. Missing app information (description, screenshots, etc.)

### "Workflow runs but doesn't publish"

**Solution**: 
The publish step only runs if secrets are configured. Check:
1. All 5 secrets are added to GitHub
2. Secret names exactly match documentation
3. Workflow run shows "Setup Play Store credentials" step

## 📊 Publishing Workflow Diagram

```
Create Git Tag (v1.0.0)
    ↓
GitHub Actions Triggered
    ↓
Build Signed APK
    ↓
[Secrets Available?]
    ├─ YES → Upload to Play Console (Internal Track)
    │         ↓
    │    ✅ Visible in Play Console
    │         ↓
    │    Manual promotion to Alpha/Beta/Production
    │
    └─ NO → Skip Play Store upload
             ↓
        ✅ Still creates GitHub Release
```

## 🎓 Learn More

- [Google Play Console Help](https://support.google.com/googleplay/android-developer)
- [Gradle Play Publisher Plugin](https://github.com/Triple-T/gradle-play-publisher)
- [Android App Bundle](https://developer.android.com/guide/app-bundle)
- [App Signing Best Practices](https://developer.android.com/studio/publish/app-signing)

## 🎉 Summary

**Quick Checklist**:

- [ ] Create Google Play Developer account
- [ ] Create app in Play Console
- [ ] Generate signing keystore
- [ ] Create service account with API access
- [ ] Add 5 secrets to GitHub repository
- [ ] Push a git tag (e.g., `v1.0.0`)
- [ ] Watch GitHub Actions build and publish
- [ ] Verify upload in Play Console
- [ ] Promote to desired track

**Once configured, every release is automatic:**
1. Push a git tag
2. APK builds and uploads automatically
3. Promote manually in Play Console

That's it! 🚀
