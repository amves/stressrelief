# Quick Reference: Release & Build Guide

## 🎯 For End Users

### Download and Install
1. Go to: https://github.com/amves/stressrelief/releases/latest
2. Download `stressrelief-X.X.X-release.apk`
3. Install on your Android phone

---

## 👨‍💻 For Developers

### Build from GitHub Actions (No Android Studio)
1. Push code: `git push`
2. Go to: https://github.com/amves/stressrelief/actions
3. Download APK from artifacts

### Build Locally
```bash
./gradlew assembleDebug
# APK at: app/build/outputs/apk/debug/app-debug.apk
```

---

## 🏷️ For Maintainers

### Create Official Release
```bash
# Create version tag
git tag v1.0.0

# Push tag (triggers automatic release)
git push origin v1.0.0
```

### What Happens Automatically
1. ✅ Tests run
2. ✅ APKs built (debug + release)
3. ✅ Changelog generated
4. ✅ GitHub Release created
5. ✅ APKs uploaded to release

### Version Numbering
- **MAJOR.MINOR.PATCH** (e.g., 1.0.0, 1.2.3, 2.0.0)
- **MAJOR**: Breaking changes
- **MINOR**: New features
- **PATCH**: Bug fixes

---

## 📚 Documentation

| What | Where |
|------|-------|
| Download releases | [Releases](https://github.com/amves/stressrelief/releases) |
| GitHub Actions builds | [GITHUB_ACTIONS_APK_BUILD.md](GITHUB_ACTIONS_APK_BUILD.md) |
| Creating releases | [RELEASE_PROCESS.md](RELEASE_PROCESS.md) |
| Local builds | [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md) |
| Health features | [WEAR_OS_HEALTH_CONNECT.md](WEAR_OS_HEALTH_CONNECT.md) |

---

## 🔗 Quick Links

- **Latest Release**: https://github.com/amves/stressrelief/releases/latest
- **All Releases**: https://github.com/amves/stressrelief/releases
- **Build Actions**: https://github.com/amves/stressrelief/actions
- **Release Workflow**: https://github.com/amves/stressrelief/actions/workflows/release.yml
