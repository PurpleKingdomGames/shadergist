package shadergistgame

import indigo._

object View:

  def present(context: FrameContext[InitialData], model: Model, viewModel: Size): Outcome[SceneUpdateFragment] =
    Outcome(
      SceneUpdateFragment(
        model match
          case Model.NoGist =>
            TextBox("No gist to load.", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.Yellow)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.Loading =>
            TextBox("Loading gist...", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.White)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.Failed =>
            val gistPath = context.startUpData.maybeGistPath.getOrElse("<missing>")
            val path     = s"https://gist.githubusercontent.com/$gistPath/raw"
            TextBox(s"Error loading gist from '$path'", viewModel.width, 20)
              .withFontFamily(FontFamily.monospace)
              .withColor(RGBA.Red)
              .withFontSize(Pixels(12))
              .alignCenter
              .moveTo(0, viewModel.height / 2)

          case Model.Ready =>
            GistEntity(viewModel)
      )
    )
