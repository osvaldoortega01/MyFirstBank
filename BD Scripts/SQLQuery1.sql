/****** Object:  Table [dbo].[ahorros]    Script Date: 16-Feb-23 9:59:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[ahorros](
	[SavingID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[Goal] [float] NOT NULL,
	[Type] [varchar](100) NOT NULL,
	[FinalDate] [date] NOT NULL,
	[CreationDate] [date] NOT NULL,
 CONSTRAINT [PK_ahorros] PRIMARY KEY CLUSTERED 
(
	[SavingID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[movimientos]    Script Date: 16-Feb-23 9:59:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[movimientos](
	[MovementID] [int] IDENTITY(1,1) NOT NULL,
	[UserID] [int] NOT NULL,
	[Description] [varchar](100) NOT NULL,
	[Type] [varchar](50) NOT NULL,
	[Quantity] [float] NOT NULL,
	[Date] [date] NOT NULL,
 CONSTRAINT [PK_movimientos] PRIMARY KEY CLUSTERED 
(
	[MovementID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[usuarios]    Script Date: 16-Feb-23 9:59:34 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[usuarios](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[FullName] [varchar](100) NOT NULL,
	[UserName] [varchar](50) NOT NULL,
	[UserPassword] [varchar](50) NOT NULL,
	[Email] [varchar](50) NOT NULL,
	[ParentalKey] [varchar](50) NOT NULL,
	[Cash] [float] NOT NULL,
	[MaxCashOut] [float] NOT NULL,
	[CreationDate] [date] NOT NULL,
 CONSTRAINT [PK_usuarios] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [dbo].[ahorros]  WITH CHECK ADD  CONSTRAINT [FK_ahorros_usuarios] FOREIGN KEY([UserID])
REFERENCES [dbo].[usuarios] ([UserID])
GO
ALTER TABLE [dbo].[ahorros] CHECK CONSTRAINT [FK_ahorros_usuarios]
GO
ALTER TABLE [dbo].[movimientos]  WITH CHECK ADD  CONSTRAINT [FK_movimientos_usuarios] FOREIGN KEY([UserID])
REFERENCES [dbo].[usuarios] ([UserID])
GO
ALTER TABLE [dbo].[movimientos] CHECK CONSTRAINT [FK_movimientos_usuarios]
GO
