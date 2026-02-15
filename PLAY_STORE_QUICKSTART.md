# Google Play Publishing - Quick Start

## What Was Added

This repository now supports **automatic publishing to Google Play Store** when you create a release!

## How It Works

When you push a git tag (e.g., `v1.0.0`), the GitHub Actions workflow will:

1. ✅ Build signed APKs
2. ✅ Upload to Google Play Console (Internal Track)
3. ✅ Create GitHub Release

## Setup (One-Time)

To enable Google Play publishing, you need to configure 5 GitHub Secrets:

### Required Secrets

| Secret Name | Description |
|------------|-------------|
| `KEYSTORE_BASE64` | Your app signing keystore (base64 encoded) |
| `KEYSTORE_PASSWORD` | Password for the keystore |
| `KEY_ALIAS` | Alias name for the signing key |
| `KEY_PASSWORD` | Password for the signing key |
| `PLAY_STORE_SERVICE_ACCOUNT_JSON` | Google Play API service account JSON |

### Where to Add Secrets

1. Go to your repository on GitHub
2. Click **Settings** → **Secrets and variables** → **Actions**
3. Click **New repository secret**
4. Add each of the 5 secrets listed above

## Full Setup Instructions

For detailed step-by-step instructions on:
- Creating a signing keystore
- Setting up Google Play service account
- Configuring secrets
- Troubleshooting

See: **[GOOGLE_PLAY_PUBLISHING.md](GOOGLE_PLAY_PUBLISHING.md)**

## What Happens Without Setup

If you don't configure the secrets, releases will still work:
- ✅ APKs are built
- ✅ GitHub Releases are created
- ⏭️ Google Play upload is skipped

The workflow gracefully handles missing secrets and continues with GitHub releases only.

## After Publishing

Once uploaded to Google Play Internal Track, you can:
1. Go to [Google Play Console](https://play.google.com/console)
2. Find your app
3. Navigate to **Release** → **Testing** → **Internal testing**
4. Promote to other tracks (Closed Testing, Open Testing, Production)

## Support

- **Full Documentation**: [GOOGLE_PLAY_PUBLISHING.md](GOOGLE_PLAY_PUBLISHING.md)
- **Release Process**: [RELEASE_PROCESS.md](RELEASE_PROCESS.md)
- **Issues**: Contact repository maintainers

---

**Ready to publish?** Follow the setup guide and push your first release tag! 🚀
