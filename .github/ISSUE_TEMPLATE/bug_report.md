name: Bug report
description: Report a bug or unexpected behavior
title: ""
labels: ["bug"]
body:
  - type: markdown
    attributes:
      value: |
        Thanks for taking the time to report a bug. Please fill out the form below as completely as possible.

  - type: input
    id: minecraft-version
    attributes:
      label: Minecraft version
      description: The version of Minecraft you are playing on.
      placeholder: 1.20.1
    validations:
      required: true

  - type: input
    id: mod-version
    attributes:
      label: Mod version
      description: The version of Strawberry Flavored you are using.
    validations:
      required: true

  - type: input
    id: java-version
    attributes:
      label: Java version
      description: The Java version used to launch the game.
      placeholder: 21.0.2

  - type: dropdown
    id: environment
    attributes:
      label: Environment
      description: Where did the issue occur?
      options:
        - Singleplayer
        - Multiplayer
        - Dedicated server
    validations:
      required: true

  - type: textarea
    id: bug-description
    attributes:
      label: What happened?
      description: Describe the bug, the steps to reproduce it, and any relevant context.
      placeholder: |
        1. I started the game...
        2. I placed...
        3. The game crashed...
    validations:
      required: true

  - type: textarea
    id: expected-behavior
    attributes:
      label: Expected behavior
      description: What did you expect to happen instead?
    validations:
      required: true

  - type: textarea
    id: logs
    attributes:
      label: Logs / crash report
      description: Paste relevant log lines or a crash report here. You can also attach files below.
      render: shell

  - type: textarea
    id: mod-list
    attributes:
      label: Relevant mods
      description: List other mods installed that may be involved.
      placeholder: Fabric API, Sodium, etc.

  - type: checkboxes
    id: checklist
    attributes:
      label: Checklist
      options:
        - label: I have searched for an existing issue related to this bug.
          required: true
        - label: I have attached logs, screenshots, or a crash report if available.
          required: false
